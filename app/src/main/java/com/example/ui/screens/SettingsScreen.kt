package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GlassBox
import com.example.ui.theme.*

@Composable
fun SettingsScreen(
    isDemoMode: Boolean,
    blockedCount: Int,
    reportsCount: Int,
    onToggleDemoMode: (Boolean) -> Unit,
    onShowFirebaseDialog: () -> Unit,
    onLogout: () -> Unit,
    onBack: () -> Unit
) {
    var notificationsEnabled by remember { mutableStateOf(true) }
    var privateAccount by remember { mutableStateOf(false) }
    var showCacheClearedSnackbar by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("settings_screen")
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
            Text(
                text = "Settings & Privacy",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }

        HorizontalDivider(color = NexoraGlassBorder)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Section: Firebase Backend
            SettingsSectionHeader(title = "CLOUD BACKEND & INFRASTRUCTURE")
            GlassBox(modifier = Modifier.fillMaxWidth()) {
                Column {
                    SettingsRow(
                        icon = Icons.Default.CloudQueue,
                        title = "Firebase Integration Setup",
                        subtitle = "Instructions to connect Firestore & Authentication",
                        trailing = {
                            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = NexoraNeonCyan)
                        },
                        onClick = onShowFirebaseDialog
                    )
                    HorizontalDivider(color = NexoraGlassBorder.copy(alpha = 0.4f))
                    SettingsRow(
                        icon = Icons.Default.Science,
                        title = "Interactive Demo Mode",
                        subtitle = if (isDemoMode) "Active (Simulated social backend)" else "Disabled (Live cloud only)",
                        trailing = {
                            Switch(
                                checked = isDemoMode,
                                onCheckedChange = onToggleDemoMode,
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = NexoraPurple
                                )
                            )
                        }
                    )
                }
            }

            // Section: Privacy & Safety
            SettingsSectionHeader(title = "PRIVACY & SAFETY")
            GlassBox(modifier = Modifier.fillMaxWidth()) {
                Column {
                    SettingsRow(
                        icon = Icons.Default.Lock,
                        title = "Private Account",
                        subtitle = "Only approved followers can view your posts",
                        trailing = {
                            Switch(
                                checked = privateAccount,
                                onCheckedChange = { privateAccount = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = NexoraPurple
                                )
                            )
                        }
                    )
                    HorizontalDivider(color = NexoraGlassBorder.copy(alpha = 0.4f))
                    SettingsRow(
                        icon = Icons.Default.Block,
                        title = "Blocked Users",
                        subtitle = "$blockedCount users currently blocked",
                        trailing = {
                            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = NexoraTextSecondary)
                        }
                    )
                    HorizontalDivider(color = NexoraGlassBorder.copy(alpha = 0.4f))
                    SettingsRow(
                        icon = Icons.Default.Security,
                        title = "Moderation & Safety Center",
                        subtitle = "$reportsCount reports filed",
                        trailing = {
                            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = NexoraTextSecondary)
                        }
                    )
                }
            }

            // Section: Notifications & Data
            SettingsSectionHeader(title = "PREFERENCES & STORAGE")
            GlassBox(modifier = Modifier.fillMaxWidth()) {
                Column {
                    SettingsRow(
                        icon = Icons.Default.Notifications,
                        title = "Push Notifications",
                        subtitle = "Likes, comments, follower alerts",
                        trailing = {
                            Switch(
                                checked = notificationsEnabled,
                                onCheckedChange = { notificationsEnabled = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = NexoraPurple
                                )
                            )
                        }
                    )
                    HorizontalDivider(color = NexoraGlassBorder.copy(alpha = 0.4f))
                    SettingsRow(
                        icon = Icons.Default.CleaningServices,
                        title = "Clear Media Cache",
                        subtitle = if (showCacheClearedSnackbar) "Cache cleared successfully (0 KB)!" else "Free up temporary storage",
                        onClick = { showCacheClearedSnackbar = true }
                    )
                }
            }

            // Section: About
            SettingsSectionHeader(title = "ABOUT NEXORA")
            GlassBox(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(text = "NEXORA – Connect Beyond Limits", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(text = "Version 1.0.0 (Production Build)", color = NexoraTextSecondary, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Package: com.nexora.social", color = NexoraNeonCyan, fontSize = 11.sp)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Logout Button
            OutlinedButton(
                onClick = onLogout,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = NexoraHeartRed),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("logout_button")
            ) {
                Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Log Out", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
private fun SettingsSectionHeader(title: String) {
    Text(
        text = title,
        color = NexoraTextMuted,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp,
        modifier = Modifier.padding(start = 4.dp)
    )
}

@Composable
private fun SettingsRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    trailing: (@Composable () -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = NexoraPurple,
                modifier = Modifier.size(22.dp)
            )
            Column {
                Text(text = title, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                Text(text = subtitle, color = NexoraTextSecondary, fontSize = 11.sp)
            }
        }
        if (trailing != null) {
            trailing()
        }
    }
}
