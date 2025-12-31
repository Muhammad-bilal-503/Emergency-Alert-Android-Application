package com.example.emergencyalertandroidapplication.data.database.dao

import androidx.room.*
import com.example.emergencyalertandroidapplication.data.model.EmergencyAlert
import kotlinx.coroutines.flow.Flow

@Dao
interface EmergencyAlertDao {
    @Query("SELECT * FROM emergency_alerts WHERE userId = :userId ORDER BY timestamp DESC")
    fun getAllAlerts(userId: String): Flow<List<EmergencyAlert>>

    @Query("SELECT * FROM emergency_alerts WHERE id = :id")
    suspend fun getAlertById(id: Long): EmergencyAlert?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(alert: EmergencyAlert): Long

    @Query("DELETE FROM emergency_alerts WHERE userId = :userId")
    suspend fun deleteAllAlerts(userId: String)
}
