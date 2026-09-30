package com.example.myapplication

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

import com.example.myapplication.components.InfoItem
import com.example.myapplication.components.ProfileCard
import com.example.myapplication.components.ProfileHeader
import kotlinx.coroutines.launch
import myapplication.shared.generated.resources.Res
import myapplication.shared.generated.resources.foto_sagab
import myapplication.shared.generated.resources.ic_edit
import myapplication.shared.generated.resources.ic_email
import myapplication.shared.generated.resources.ic_location
import myapplication.shared.generated.resources.ic_phone
import myapplication.shared.generated.resources.ic_share
import myapplication.shared.generated.resources.profile_avatar
import org.jetbrains.compose.resources.painterResource

@Composable
fun ProfileScreen() {
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val bioText by remember {
        mutableStateOf(
            "Mahasiswa Teknik Informatika yang memiliki ketertarikan pada jaringan komputer"
        )
    }
    var isFollowing by remember { mutableStateOf(false) }

    fun showMessage(msg: String) {
        scope.launch {
            snackbarHostState.showSnackbar(msg)
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

            // 1. Reusable ProfileHeader (Circular photo, name, title, online badge)
            ProfileHeader(
                name = "BAGAS DWI AJITYA",
                role = "Mahasiswa Teknik Informatika",
                painter = painterResource(Res.drawable.foto_sagab),
                isOnline = true,
                imageRotation = 90f
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Action Buttons Row (Button, OutlinedButton, Row layout)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        isFollowing = !isFollowing
                        showMessage(if (isFollowing) "Following profile!" else "Unfollowed profile.")
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
                    Text(if (isFollowing) "Following" else "Edit Profile")
                }

                Spacer(modifier = Modifier.width(12.dp))

                OutlinedButton(
                    onClick = {
                        showMessage("Profile link copied to clipboard!")
                    },
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

            // Quick Stats Row (Card, Column, Row layout)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatCard(label = "Projects", value = "soon", modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(8.dp))
                StatCard(label = "Experience", value = "soon", modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(8.dp))
                StatCard(label = "Rating", value = "soon", modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 2. Reusable ProfileCard for Bio / Deskripsi singkat
            ProfileCard(
                title = "About Me"
            ) {
                Text(
                    text = bioText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.2f
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 3. Reusable ProfileCard for List Informasi (Email, Phone, Location)
            ProfileCard(
                title = "Contact Information"
            ) {
                // Reusable InfoItem for Email
                InfoItem(
                    iconPainter = painterResource(Res.drawable.ic_email),
                    label = "Email",
                    value = "bagasdwiajitya@gmail.com",
                    onClick = { showMessage("Copied Email: bagasdwiajitya@gmail.com") }
                )

                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    color = DividerDefaults.color.copy(alpha = 0.3f)
                )

                // Reusable InfoItem for Phone
                InfoItem(
                    iconPainter = painterResource(Res.drawable.ic_phone),
                    label = "Phone",
                    value = "+62 813-8045-4670",
                    onClick = { showMessage("Calling +62 813-8045-4670...") }
                )

                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    color = DividerDefaults.color.copy(alpha = 0.3f)
                )

                // Reusable InfoItem for Location
                InfoItem(
                    iconPainter = painterResource(Res.drawable.ic_location),
                    label = "Location",
                    value = "Jakarta, Indonesia",
                    onClick = { showMessage("Opening location in Maps...") }
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
