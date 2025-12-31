package com.example.emergencyalertandroidapplication.data.model

enum class EmergencyType(val displayName: String, val emoji: String) {
    MEDICAL("Medical Emergency", "🏥"),
    SAFETY("Safety Threat", "🛡️"),
    ACCIDENT("Accident", "🚗"),
    FIRE("Fire Emergency", "🔥")
}
