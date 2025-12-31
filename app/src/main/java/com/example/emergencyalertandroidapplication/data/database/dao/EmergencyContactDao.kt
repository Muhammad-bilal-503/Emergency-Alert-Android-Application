package com.example.emergencyalertandroidapplication.data.database.dao

import androidx.room.*
import com.example.emergencyalertandroidapplication.data.model.EmergencyContact
import kotlinx.coroutines.flow.Flow

@Dao
interface EmergencyContactDao {
    @Query("SELECT * FROM emergency_contacts WHERE userId = :userId ORDER BY name ASC")
    fun getAllContacts(userId: String): Flow<List<EmergencyContact>>

    @Query("SELECT * FROM emergency_contacts WHERE id = :id")
    suspend fun getContactById(id: Long): EmergencyContact?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contact: EmergencyContact): Long

    @Update
    suspend fun updateContact(contact: EmergencyContact)

    @Delete
    suspend fun deleteContact(contact: EmergencyContact)

    @Query("SELECT COUNT(*) FROM emergency_contacts WHERE userId = :userId")
    suspend fun getContactCount(userId: String): Int
}
