package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ProfileViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun toggleDarkMode(enabled: Boolean) {
        _uiState.update { it.copy(isDarkMode = enabled) }
    }

    fun startEditing() {
        _uiState.update { currentState ->
            currentState.copy(
                isEditing = true,
                editName = currentState.profile.name,
                editBio = currentState.profile.bio
            )
        }
    }

    fun cancelEditing() {
        _uiState.update { currentState ->
            currentState.copy(
                isEditing = false,
                editName = currentState.profile.name,
                editBio = currentState.profile.bio
            )
        }
    }

    fun onNameChange(newName: String) {
        _uiState.update { it.copy(editName = newName) }
    }

    fun onBioChange(newBio: String) {
        _uiState.update { it.copy(editBio = newBio) }
    }

    fun saveProfile() {
        _uiState.update { currentState ->
            val updatedProfile = currentState.profile.copy(
                name = currentState.editName.ifBlank { currentState.profile.name },
                bio = currentState.editBio
            )
            currentState.copy(
                profile = updatedProfile,
                isEditing = false,
                snackbarMessage = "Profil berhasil diperbarui!"
            )
        }
    }

    fun snackbarMessageShown() {
        _uiState.update { it.copy(snackbarMessage = null) }
    }
}
