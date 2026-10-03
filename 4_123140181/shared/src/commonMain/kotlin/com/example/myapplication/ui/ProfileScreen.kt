package com.example.myapplication.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

import com.example.myapplication.ui.components.DarkModeToggleCard
import com.example.myapplication.ui.components.EditProfileForm
import com.example.myapplication.ui.components.InfoItem
import com.example.myapplication.ui.components.ProfileCard
import com.example.myapplication.ui.components.ProfileHeader
import com.example.myapplication.viewmodel.ProfileUiState
import myapplication.shared.generated.resources.Res
import myapplication.shared.generated.resources.foto_sagab
import myapplication.shared.generated.resources.ic_edit
import myapplication.shared.generated.resources.ic_email
import myapplication.shared.generated.resources.ic_location
import myapplication.shared.generated.resources.ic_phone
import myapplication.shared.generated.resources.ic_share
import org.jetbrains.compose.resources.painterResource

@Composable
fun ProfileScreen(
    uiState: ProfileUiState,
    onNameChange: (String) -> Unit,
    onBioChange: (String) -> Unit,
    onStartEdit: () -> Unit,
    onCancelEdit: () -> Unit,
    onSaveProfile: () -> Unit,
    onToggleDarkMode: (Boolean) -> Unit,
    onSnackbarShown: () -> Unit
) {
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }

    val profile = uiState.profile

    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            onSnackbarShown()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Dark Mode Toggle Switch Card
            DarkModeToggleCard(
                isDarkMode = uiState.isDarkMode,
                onToggleDarkMode = onToggleDarkMode
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Profile Header
            ProfileHeader(
                name = profile.name,
                role = profile.role,
                painter = painterResource(Res.drawable.foto_sagab),
                isOnline = true,
                imageRotation = 90f
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Action Buttons Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        if (uiState.isEditing) onCancelEdit() else onStartEdit()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_edit),
                        contentDescription = "Edit Profile",
                        modifier = Modifier
                            .padding(end = 6.dp)
                            .size(18.dp)
                    )
                    Text(if (uiState.isEditing) "Tutup Edit" else "Edit Profile")
                }

                Spacer(modifier = Modifier.width(12.dp))

                OutlinedButton(
                    onClick = { /* Share profile functionality */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_share),
                        contentDescription = "Share Profile",
                        modifier = Modifier
                            .padding(end = 6.dp)
                            .size(18.dp)
                    )
                    Text("Share Profile")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Animated Form Edit Profile
            AnimatedVisibility(visible = uiState.isEditing) {
                Column {
                    EditProfileForm(
                        name = uiState.editName,
                        bio = uiState.editBio,
                        onNameChange = onNameChange,
                        onBioChange = onBioChange,
                        onSave = onSaveProfile,
                        onCancel = onCancelEdit
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }

            // Quick Stats Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatCard(label = "Projects", value = "${profile.projectsCount}", modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(8.dp))
                StatCard(label = "Experience", value = "${profile.experienceYears} thn", modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(8.dp))
                StatCard(label = "Rating", value = "${profile.rating}", modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Profile Bio Card
            ProfileCard(
                title = "About Me"
            ) {
                Text(
                    text = profile.bio,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.2f
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Contact Info Card
            ProfileCard(
                title = "Contact Information"
            ) {
                InfoItem(
                    iconPainter = painterResource(Res.drawable.ic_email),
                    label = "Email",
                    value = profile.email
                )

                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    color = DividerDefaults.color.copy(alpha = 0.3f)
                )

                InfoItem(
                    iconPainter = painterResource(Res.drawable.ic_phone),
                    label = "Phone",
                    value = profile.phone
                )

                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    color = DividerDefaults.color.copy(alpha = 0.3f)
                )

                InfoItem(
                    iconPainter = painterResource(Res.drawable.ic_location),
                    label = "Location",
                    value = profile.location
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )
    }
}

@Composable
private fun StatCard(label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
        )
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}
