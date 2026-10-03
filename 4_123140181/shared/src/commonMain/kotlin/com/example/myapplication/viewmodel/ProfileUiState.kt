package com.example.myapplication.viewmodel

import com.example.myapplication.data.Profile

data class ProfileUiState(
    val profile: Profile = Profile(),
    val isDarkMode: Boolean = false,
    val isEditing: Boolean = false,
    val editName: String = profile.name,
    val editBio: String = profile.bio,
    val snackbarMessage: String? = null
)
