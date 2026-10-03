package com.example.myapplication

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.ui.ProfileScreen
import com.example.myapplication.viewmodel.ProfileViewModel

@Composable
fun App(viewModel: ProfileViewModel = viewModel { ProfileViewModel() }) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val colorScheme = if (uiState.isDarkMode) {
        darkColorScheme()
    } else {
        lightColorScheme()
    }

    MaterialTheme(colorScheme = colorScheme) {
        ProfileScreen(
            uiState = uiState,
            onNameChange = viewModel::onNameChange,
            onBioChange = viewModel::onBioChange,
            onStartEdit = viewModel::startEditing,
            onCancelEdit = viewModel::cancelEditing,
            onSaveProfile = viewModel::saveProfile,
            onToggleDarkMode = viewModel::toggleDarkMode,
            onSnackbarShown = viewModel::snackbarMessageShown
        )
    }
}
