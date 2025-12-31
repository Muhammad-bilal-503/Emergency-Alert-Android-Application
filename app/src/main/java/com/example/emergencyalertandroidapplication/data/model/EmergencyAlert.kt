package com.example.emergencyalertandroidapplication.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.emergencyalertandroidapplication.data.database.Converters

@Entity(tableName = "emergency_alerts")
@TypeConverters(Converters::class)
data class EmergencyAlert(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: String, // Firebase user ID
    val emergencyType: EmergencyType,
    val latitude: Double,
    val longitude: Double,
    val address: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val message: String,
    val firebaseId: String? = null // For Firebase sync
)
