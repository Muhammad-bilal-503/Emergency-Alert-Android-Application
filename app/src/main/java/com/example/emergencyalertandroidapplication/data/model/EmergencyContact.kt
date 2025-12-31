package com.example.emergencyalertandroidapplication.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "emergency_contacts")
data class EmergencyContact(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val phoneNumber: String,
    val email: String? = null,
    val userId: String, // Firebase user ID
    val firebaseId: String? = null // For Firebase sync
)
