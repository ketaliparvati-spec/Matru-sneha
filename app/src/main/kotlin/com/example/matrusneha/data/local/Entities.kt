package com.example.matrusneha.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mother_profile")
data class MotherProfile(
    @PrimaryKey val id: Int = 1, // Single profile app
    val name: String,
    val age: Int,
    val village: String,
    val pregnancyMonth: Int,
    val phoneNumber: String,
    val bloodGroup: String,
    val doctorNumber: String = "108",
    val ashaWorkerNumber: String = "",
    val familyNumber: String = ""
)

@Entity(tableName = "kick_log")
data class KickLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long,
    val dateString: String // e.g., "2024-05-15"
)

@Entity(tableName = "nutrition_log")
data class NutritionLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val foodName: String,
    val category: String, // e.g., "Energy", "Protein", "Iron"
    val dateString: String,
    val isCompleted: Boolean
)

@Entity(tableName = "reminder")
data class Reminder(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val date: String,
    val timestamp: Long = System.currentTimeMillis()
)
