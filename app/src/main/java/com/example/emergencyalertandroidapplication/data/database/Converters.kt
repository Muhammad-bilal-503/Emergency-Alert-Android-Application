package com.example.emergencyalertandroidapplication.data.database

import androidx.room.TypeConverter
import com.example.emergencyalertandroidapplication.data.model.EmergencyType

class Converters {
    @TypeConverter
    fun fromEmergencyType(value: EmergencyType): String {
        return value.name
    }

    @TypeConverter
    fun toEmergencyType(value: String): EmergencyType {
        return EmergencyType.valueOf(value)
    }
}
