package com.example.emergencyalertandroidapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emergencyalertandroidapplication.data.model.EmergencyAlert
import com.example.emergencyalertandroidapplication.data.repository.AlertRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class HistoryUiState(
    val alerts: List<EmergencyAlert> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class HistoryViewModel(
    private val alertRepository: AlertRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    private val userId: String
        get() = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    init {
        loadAlerts()
    }

    private fun loadAlerts() {
        viewModelScope.launch {
            alertRepository.getAllAlerts(userId).collectLatest { alerts ->
                _uiState.value = _uiState.value.copy(alerts = alerts)
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}
