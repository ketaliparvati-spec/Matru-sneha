package com.example.matrusneha.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MatruSnehaDao {

    // Mother Profile
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: MotherProfile)

    @Query("SELECT * FROM mother_profile WHERE id = 1 LIMIT 1")
    fun getProfile(): Flow<MotherProfile?>

    @Query("DELETE FROM mother_profile")
    suspend fun clearProfile()

    // Kick Log
    @Insert
    suspend fun insertKick(kickLog: KickLog)

    @Query("SELECT COUNT(*) FROM kick_log WHERE dateString = :date")
    fun getKickCountForDate(date: String): Flow<Int>

    @Query("SELECT COUNT(*) FROM kick_log WHERE dateString = :date")
    suspend fun getKickCountForDateSync(date: String): Int

    @Query("SELECT * FROM kick_log ORDER BY timestamp DESC LIMIT 50")
    fun getRecentKicks(): Flow<List<KickLog>>

    // Nutrition Log
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNutritionLog(log: NutritionLog)

    @Query("SELECT * FROM nutrition_log WHERE dateString = :date")
    fun getNutritionLogsForDate(date: String): Flow<List<NutritionLog>>

    // Reminders
    @Insert
    suspend fun insertReminder(reminder: Reminder)

    @Query("SELECT * FROM reminder ORDER BY timestamp ASC")
    fun getAllReminders(): Flow<List<Reminder>>
}
