package com.example.matrusneha.data.repository

import com.example.matrusneha.data.local.KickLog
import com.example.matrusneha.data.local.MatruSnehaDao
import com.example.matrusneha.data.local.MotherProfile
import kotlinx.coroutines.flow.Flow

class MatruSnehaRepository(private val dao: MatruSnehaDao) {

    // Profile
    val profile: Flow<MotherProfile?> = dao.getProfile()

    suspend fun saveProfile(profile: MotherProfile) {
        dao.insertProfile(profile)
    }

    suspend fun clearProfile() {
        dao.clearProfile()
    }

    // Kicks
    fun getTodayKickCount(dateString: String): Flow<Int> {
        return dao.getKickCountForDate(dateString)
    }

    suspend fun getKickCountForDate(dateString: String): Int {
        return dao.getKickCountForDateSync(dateString)
    }

    suspend fun recordKick(kickLog: KickLog) {
        dao.insertKick(kickLog)
    }

    // Nutrition
    fun getTodayNutritionLogs(dateString: String): Flow<List<com.example.matrusneha.data.local.NutritionLog>> {
        return dao.getNutritionLogsForDate(dateString)
    }

    suspend fun saveNutritionLog(log: com.example.matrusneha.data.local.NutritionLog) {
        dao.insertNutritionLog(log)
    }

    // Reminders
    fun getReminders(): Flow<List<com.example.matrusneha.data.local.Reminder>> {
        return dao.getAllReminders()
    }

    suspend fun saveReminder(reminder: com.example.matrusneha.data.local.Reminder) {
        dao.insertReminder(reminder)
    }
}
