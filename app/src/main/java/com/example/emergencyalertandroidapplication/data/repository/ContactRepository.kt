package com.example.emergencyalertandroidapplication.data.repository

import com.example.emergencyalertandroidapplication.data.database.dao.EmergencyContactDao
import com.example.emergencyalertandroidapplication.data.model.EmergencyContact
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

class ContactRepository(
    private val contactDao: EmergencyContactDao,
    private val firestore: FirebaseFirestore
) {
    fun getAllContacts(userId: String): Flow<List<EmergencyContact>> {
        return contactDao.getAllContacts(userId)
    }

    suspend fun getContactById(id: Long): EmergencyContact? {
        return contactDao.getContactById(id)
    }

    suspend fun insertContact(contact: EmergencyContact): Result<Long> {
        return try {
            val localId = contactDao.insertContact(contact)
            
            // Sync to Firebase
            val firebaseId = firestore.collection("contacts")
                .add(contact.copy(id = localId))
                .await()
                .id
            
            // Update local contact with Firebase ID
            contactDao.updateContact(contact.copy(id = localId, firebaseId = firebaseId))
            
            Result.success(localId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateContact(contact: EmergencyContact): Result<Unit> {
        return try {
            contactDao.updateContact(contact)
            
            // Sync to Firebase
            contact.firebaseId?.let { firebaseId ->
                firestore.collection("contacts")
                    .document(firebaseId)
                    .set(contact)
                    .await()
            }
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteContact(contact: EmergencyContact): Result<Unit> {
        return try {
            contactDao.deleteContact(contact)
            
            // Delete from Firebase
            contact.firebaseId?.let { firebaseId ->
                firestore.collection("contacts")
                    .document(firebaseId)
                    .delete()
                    .await()
            }
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getContactCount(userId: String): Int {
        return contactDao.getContactCount(userId)
    }
}
