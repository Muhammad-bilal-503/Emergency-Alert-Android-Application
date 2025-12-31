package com.example.emergencyalertandroidapplication.data.model

data class LocationData(
    val latitude: Double,
    val longitude: Double,
    val address: String? = null
) {
    fun toGoogleMapsLink(): String {
        return "https://www.google.com/maps?q=$latitude,$longitude"
    }
}
