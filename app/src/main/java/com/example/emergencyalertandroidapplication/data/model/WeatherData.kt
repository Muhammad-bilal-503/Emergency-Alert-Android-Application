package com.example.emergencyalertandroidapplication.data.model

data class WeatherData(
    val temperature: Double,
    val description: String,
    val humidity: Int,
    val windSpeed: Double,
    val city: String
)
