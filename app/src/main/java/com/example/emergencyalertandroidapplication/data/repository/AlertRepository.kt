package com.example.emergencyalertandroidapplication.data.repository

import com.example.emergencyalertandroidapplication.data.database.dao.EmergencyAlertDao
import com.example.emergencyalertandroidapplication.data.model.EmergencyAlert
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

class AlertRepository(
    private val alertDao: EmergencyAlertDao,
    private val firestore: FirebaseFirestore
) {
    fun getAllAlerts(userId: String): Flow<List<EmergencyAlert>> {
        return alertDao.getAllAlerts(userId)
    }

    suspend fun insertAlert(alert: EmergencyAlert): Result<Long> {
        return try {
            val localId = alertDao.insertAlert(alert)
            
            // Sync to Firebase
            val firebaseId = firestore.collection("alerts")
                .add(alert.copy(id = localId))
                .await()
                .id
            
            // Update local alert with Firebase ID
            alertDao.insertAlert(alert.copy(id = localId, firebaseId = firebaseId))
            
            Result.success(localId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
