package com.example.emergencyalertandroidapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emergencyalertandroidapplication.data.model.EmergencyContact
import com.example.emergencyalertandroidapplication.data.repository.ContactRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class ContactsUiState(
    val contacts: List<EmergencyContact> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val contactCount: Int = 0
)

class ContactsViewModel(
    private val contactRepository: ContactRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ContactsUiState())
    val uiState: StateFlow<ContactsUiState> = _uiState.asStateFlow()

    private val userId: String
        get() = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    init {
        loadContacts()
    }

    private fun loadContacts() {
        viewModelScope.launch {
            contactRepository.getAllContacts(userId).collectLatest { contacts ->
                _uiState.value = _uiState.value.copy(
                    contacts = contacts,
                    contactCount = contacts.size
                )
            }
        }
    }

    suspend fun canAddMoreContacts(): Boolean {
        return contactRepository.getContactCount(userId) < 5
    }

    fun addContact(contact: EmergencyContact) {
        viewModelScope.launch {
            if (!canAddMoreContacts()) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Maximum 5 contacts allowed"
                )
                return@launch
            }

            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            contactRepository.insertContact(contact.copy(userId = userId))
                .onSuccess {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = exception.message ?: "Failed to add contact"
                    )
                }
        }
    }

    fun updateContact(contact: EmergencyContact) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            contactRepository.updateContact(contact)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = exception.message ?: "Failed to update contact"
                    )
                }
        }
    }

    fun deleteContact(contact: EmergencyContact) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            contactRepository.deleteContact(contact)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = exception.message ?: "Failed to delete contact"
                    )
                }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}
