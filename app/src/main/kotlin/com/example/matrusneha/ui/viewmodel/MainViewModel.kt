package com.example.matrusneha.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.matrusneha.data.local.AppDatabase
import com.example.matrusneha.data.local.KickLog
import com.example.matrusneha.data.local.MotherProfile
import com.example.matrusneha.data.local.NutritionLog
import com.example.matrusneha.data.local.Reminder
import com.example.matrusneha.data.repository.MatruSnehaRepository
import com.example.matrusneha.data.ai.AiSuggestionService
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Calendar

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MatruSnehaRepository
    private val aiService = AiSuggestionService()

    private val _aiSuggestion = MutableStateFlow<String?>(null)
    val aiSuggestion: StateFlow<String?> = _aiSuggestion

    init {
        val dao = AppDatabase.getDatabase(application).matruSnehaDao()
        repository = MatruSnehaRepository(dao)
    }

    val reminders: StateFlow<List<Reminder>> = repository.getReminders().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Profile State
    val profile: StateFlow<MotherProfile?> = repository.profile.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    init {
        viewModelScope.launch {
            profile.collect {
                if (it != null) generateNewSuggestion()
            }
        }
    }

    // Today's Date String for querying
    private val todayString: String
        get() = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    // Today's Kick Count State
    val todayKickCount: StateFlow<Int> = repository.getTodayKickCount(todayString).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )

    private val _last7DaysKicks = MutableStateFlow<List<Float>>(listOf(0f, 0f, 0f, 0f, 0f, 0f, 0f))
    val last7DaysKicks: StateFlow<List<Float>> = _last7DaysKicks

    init {
        viewModelScope.launch {
            // Update 7-day history whenever today's kick count changes
            todayKickCount.collect {
                val history = mutableListOf<Float>()
                val cal = Calendar.getInstance()
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                
                for (i in 6 downTo 1) {
                    val pastCal = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -i) }
                    val count = repository.getKickCountForDate(sdf.format(pastCal.time))
                    history.add(count.toFloat())
                }
                history.add(it.toFloat()) // Add today's live count
                _last7DaysKicks.value = history
            }
        }
    }

    fun saveProfile(name: String, age: Int, village: String, pregnancyMonth: Int, phone: String, bloodGroup: String) {
        viewModelScope.launch {
            val newProfile = MotherProfile(
                id = 1,
                name = name,
                age = age,
                village = village,
                pregnancyMonth = pregnancyMonth,
                phoneNumber = phone,
                bloodGroup = bloodGroup
            )
            repository.saveProfile(newProfile)
        }
    }

    fun saveEmergencyContacts(doctor: String, asha: String, family: String) {
        viewModelScope.launch {
            val currentProfile = profile.value ?: return@launch
            val updatedProfile = currentProfile.copy(
                doctorNumber = doctor,
                ashaWorkerNumber = asha,
                familyNumber = family
            )
            repository.saveProfile(updatedProfile)
        }
    }

    fun saveReminder(title: String, date: String) {
        viewModelScope.launch {
            val reminder = com.example.matrusneha.data.local.Reminder(
                title = title,
                date = date
            )
            repository.saveReminder(reminder)
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.clearProfile()
        }
    }

    fun recordKick() {
        viewModelScope.launch {
            val kick = KickLog(
                timestamp = System.currentTimeMillis(),
                dateString = todayString
            )
            repository.recordKick(kick)
        }
    }

    // Nutrition State
    val todayNutritionLogs: StateFlow<List<NutritionLog>> = repository.getTodayNutritionLogs(todayString)
        .map { logs ->
            if (logs.isEmpty()) {
                // Initialize default daily logs if empty (Karnataka centric)
                val defaultLogs = listOf(
                    NutritionLog(foodName = "Ragi Mudde / Jowar Roti", category = "Energy Foods", dateString = todayString, isCompleted = false),
                    NutritionLog(foodName = "Anna-Saru (Rice & Sambar)", category = "Energy Foods", dateString = todayString, isCompleted = false),
                    NutritionLog(foodName = "Millet / Chapati", category = "Energy Foods", dateString = todayString, isCompleted = false),
                    NutritionLog(foodName = "Eggs", category = "Protein Foods", dateString = todayString, isCompleted = false),
                    NutritionLog(foodName = "Togari Bele (Pulses)", category = "Protein Foods", dateString = todayString, isCompleted = false),
                    NutritionLog(foodName = "Milk / Curd", category = "Protein Foods", dateString = todayString, isCompleted = false),
                    NutritionLog(foodName = "Soppu (Green Leaves)", category = "Iron Foods", dateString = todayString, isCompleted = false),
                    NutritionLog(foodName = "Jaggery / Beetroot", category = "Iron Foods", dateString = todayString, isCompleted = false)
                )
                defaultLogs.forEach { repository.saveNutritionLog(it) }
                defaultLogs
            } else {
                logs
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList<NutritionLog>()
        )

    fun toggleNutritionLog(log: NutritionLog, isChecked: Boolean) {
        viewModelScope.launch {
            repository.saveNutritionLog(log.copy(isCompleted = isChecked))
            generateNewSuggestion() // Regenerate suggestion when state changes
        }
    }

    fun generateNewSuggestion() {
        viewModelScope.launch {
            val prof = profile.value ?: return@launch
            val week = prof.pregnancyMonth * 4
            val kicks = todayKickCount.value
            val logs = todayNutritionLogs.value
            
            _aiSuggestion.value = aiService.getDailyHealthSuggestion(week, kicks, logs)
        }
    }
}
