package com.example.emergencyalertandroidapplication.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.emergencyalertandroidapplication.data.database.dao.EmergencyAlertDao
import com.example.emergencyalertandroidapplication.data.database.dao.EmergencyContactDao
import com.example.emergencyalertandroidapplication.data.model.EmergencyAlert
import com.example.emergencyalertandroidapplication.data.model.EmergencyContact

@Database(
    entities = [EmergencyContact::class, EmergencyAlert::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class EmergencyDatabase : RoomDatabase() {
    abstract fun emergencyContactDao(): EmergencyContactDao
    abstract fun emergencyAlertDao(): EmergencyAlertDao

    companion object {
        @Volatile
        private var INSTANCE: EmergencyDatabase? = null

        fun getDatabase(context: Context): EmergencyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EmergencyDatabase::class.java,
                    "emergency_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
