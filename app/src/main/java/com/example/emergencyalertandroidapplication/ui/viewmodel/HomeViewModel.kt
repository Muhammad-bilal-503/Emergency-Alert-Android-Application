package com.example.emergencyalertandroidapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emergencyalertandroidapplication.data.model.EmergencyType
import com.example.emergencyalertandroidapplication.data.model.LocationData
import com.example.emergencyalertandroidapplication.data.model.WeatherData
import com.example.emergencyalertandroidapplication.data.repository.AlertRepository
import com.example.emergencyalertandroidapplication.data.repository.ContactRepository
import com.example.emergencyalertandroidapplication.data.repository.LocationRepository
import com.example.emergencyalertandroidapplication.data.repository.WeatherRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HomeUiState(
    val selectedEmergencyType: EmergencyType? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val weatherData: WeatherData? = null,
    val countdownSeconds: Int = 0,
    val isSendingAlert: Boolean = false
)

class HomeViewModel(
    private val contactRepository: ContactRepository,
    private val alertRepository: AlertRepository,
    private val locationRepository: LocationRepository,
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val userId: String
        get() = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    init {
        loadWeather()
    }

    fun selectEmergencyType(type: EmergencyType) {
        _uiState.value = _uiState.value.copy(selectedEmergencyType = type)
    }

    fun startCountdown(onComplete: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(countdownSeconds = 3)
            
            for (i in 3 downTo 1) {
                kotlinx.coroutines.delay(1000)
                _uiState.value = _uiState.value.copy(countdownSeconds = i - 1)
            }
            
            onComplete()
        }
    }

    fun sendEmergencyAlert(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val emergencyType = _uiState.value.selectedEmergencyType
            if (emergencyType == null) {
                _uiState.value = _uiState.value.copy(errorMessage = "Please select an emergency type")
                return@launch
            }

            _uiState.value = _uiState.value.copy(isSendingAlert = true, errorMessage = null)

            // Get location
            val locationResult = locationRepository.getCurrentLocation()
            if (locationResult.isFailure) {
                _uiState.value = _uiState.value.copy(
                    isSendingAlert = false,
                    errorMessage = "Failed to get location: ${locationResult.exceptionOrNull()?.message}"
                )
                return@launch
            }

            val location = locationResult.getOrNull()!!
            
            // Get contacts count to verify we have contacts
            val contactCount = contactRepository.getContactCount(userId)
            if (contactCount == 0) {
                _uiState.value = _uiState.value.copy(
                    isSendingAlert = false,
                    errorMessage = "No emergency contacts added. Please add contacts first."
                )
                return@launch
            }
            
            // Create alert message
            val message = buildEmergencyMessage(emergencyType, location)
            
            // Create alert record
            val alert = com.example.emergencyalertandroidapplication.data.model.EmergencyAlert(
                userId = userId,
                emergencyType = emergencyType,
                latitude = location.latitude,
                longitude = location.longitude,
                address = location.address,
                message = message
            )

            // Save alert
            alertRepository.insertAlert(alert)
                .onSuccess {
                    // TODO: Send notifications to contacts via FCM
                    // Implementation requires:
                    // 1. Store FCM tokens for each contact's device
                    // 2. Server-side code to send FCM messages
                    // 3. Or use Firebase Admin SDK in a backend service
                    // For now, alerts are saved locally and synced to Firestore
                    _uiState.value = _uiState.value.copy(
                        isSendingAlert = false,
                        selectedEmergencyType = null
                    )
                    onSuccess()
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isSendingAlert = false,
                        errorMessage = "Failed to send alert: ${exception.message}"
                    )
                }
        }
    }

    private fun buildEmergencyMessage(type: EmergencyType, location: LocationData): String {
        val userName = FirebaseAuth.getInstance().currentUser?.displayName ?: "User"
        val mapsLink = location.toGoogleMapsLink()
        return """
            🚨 EMERGENCY ALERT 🚨
            
            Type: ${type.displayName} ${type.emoji}
            User: $userName
            Location: ${location.address ?: "Unknown"}
            Maps: $mapsLink
            Time: ${java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault()).format(java.util.Date())}
            
            Please respond immediately!
        """.trimIndent()
    }

    private fun loadWeather() {
        viewModelScope.launch {
            // Get location first, then weather
            locationRepository.getCurrentLocation()
                .onSuccess { location ->
                    weatherRepository.getCurrentWeather(location.latitude, location.longitude)
                        .onSuccess { weather ->
                            _uiState.value = _uiState.value.copy(weatherData = weather)
                        }
                        .onFailure {
                            // Weather loading failure is not critical
                        }
                }
                .onFailure {
                    // Location failure is not critical for weather
                }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}
