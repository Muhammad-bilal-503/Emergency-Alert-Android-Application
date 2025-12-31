package com.example.emergencyalertandroidapplication.data.repository

import com.example.emergencyalertandroidapplication.BuildConfig
import com.example.emergencyalertandroidapplication.data.model.WeatherData
import com.example.emergencyalertandroidapplication.data.remote.WeatherApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherRepository(
    private val weatherApiService: WeatherApiService
) {
    suspend fun getCurrentWeather(latitude: Double, longitude: Double): Result<WeatherData> {
        return try {
            withContext(Dispatchers.IO) {
                // Using OpenWeatherMap API format (users need to add their API key)
                val apiKey = BuildConfig.WEATHER_API_KEY
                if (apiKey == "YOUR_WEATHER_API_KEY") {
                    // Return mock data if API key not configured
                    Result.success(
                        WeatherData(
                            temperature = 25.0,
                            description = "Clear sky",
                            humidity = 60,
                            windSpeed = 5.0,
                            city = "Unknown"
                        )
                    )
                } else {
                    val response = weatherApiService.getCurrentWeather(latitude, longitude, apiKey)
                    Result.success(
                        WeatherData(
                            temperature = response.main.temp - 273.15, // Convert from Kelvin
                            description = response.weather.firstOrNull()?.description ?: "Unknown",
                            humidity = response.main.humidity,
                            windSpeed = response.wind?.speed ?: 0.0,
                            city = response.name
                        )
                    )
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
