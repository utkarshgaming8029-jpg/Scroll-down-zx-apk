package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.theme.*

@Composable
fun GlassBox(
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(20.dp),
    backgroundColor: Color = NexoraGlassSurface,
    borderColor: Color = NexoraGlassBorder,
    borderWidth: Dp = 1.dp,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(backgroundColor)
            .border(borderWidth, borderColor, shape),
        content = content
    )
}

@Composable
fun GradientButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    gradient: Brush = NexoraPrimaryGradient,
    enabled: Boolean = true,
    testTag: String = "gradient_button"
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues(0.dp),
        shape = RoundedCornerShape(28.dp),
        modifier = modifier
            .minimumInteractiveComponentSize()
            .testTag(testTag)
            .height(52.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(if (enabled) gradient else Brush.linearGradient(listOf(Color.Gray, Color.DarkGray)))
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun UserAvatar(
    imageUrl: String?,
    username: String,
    size: Dp = 44.dp,
    hasStory: Boolean = false,
    hasUnseenStory: Boolean = false,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val ringPadding = if (hasStory) 3.dp else 0.dp
    val ringBorder = if (hasStory) {
        if (hasUnseenStory) NexoraStoryGradient else Brush.linearGradient(listOf(NexoraTextMuted, NexoraTextMuted))
    } else null

    Box(
        modifier = modifier
            .size(size)
            .then(
                if (ringBorder != null) {
                    Modifier
                        .border(2.dp, ringBorder, CircleShape)
                        .padding(ringPadding)
                } else Modifier
            )
            .clip(CircleShape)
            .background(NexoraSurfaceLightDark)
            .then(
                if (onClick != null) {
                    Modifier.clickable { onClick() }
                } else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        if (!imageUrl.isNullOrBlank()) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "$username's avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            val initial = username.firstOrNull()?.uppercaseChar()?.toString() ?: "N"
            Text(
                text = initial,
                color = NexoraNeonCyan,
                fontWeight = FontWeight.Bold,
                fontSize = (size.value * 0.4).sp
            )
        }
    }
}

@Composable
fun VerifiedBadge(
    size: Dp = 16.dp,
    modifier: Modifier = Modifier
) {
    Icon(
        imageVector = Icons.Default.CheckCircle,
        contentDescription = "Verified account",
        tint = NexoraVerifiedBlue,
        modifier = modifier.size(size)
    )
}

@Composable
fun DemoModeBanner(
    onInfoClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    GlassBox(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onInfoClick() }
            .testTag("demo_mode_banner"),
        shape = RoundedCornerShape(12.dp),
        backgroundColor = NexoraViolet.copy(alpha = 0.25f),
        borderColor = NexoraPurple.copy(alpha = 0.6f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(NexoraTealAccent)
                )
                Column {
                    Text(
                        text = "Demo Mode Active",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Full UI interactive. Tap for Firebase setup instructions.",
                        color = NexoraTextSecondary,
                        fontSize = 11.sp
                    )
                }
            }
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Firebase Setup Info",
                tint = NexoraNeonCyan,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
fun ReportBlockDialog(
    targetType: String,
    onDismiss: () -> Unit,
    onSubmitReport: (reason: String) -> Unit,
    onBlockUser: (() -> Unit)? = null
) {
    var selectedReason by remember { mutableStateOf("Spam or misleading") }
    val reasons = listOf(
        "Spam or misleading",
        "Hate speech or harassment",
        "Inappropriate media",
        "Violence or dangerous content",
        "Impersonation"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = NexoraSurfaceDark,
        title = {
            Text(
                text = "Report $targetType",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Help us keep Nexora safe. Why are you reporting this?",
                    color = NexoraTextSecondary,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                reasons.forEach { reason ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { selectedReason = reason }
                            .padding(vertical = 8.dp, horizontal = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedReason == reason,
                            onClick = { selectedReason = reason },
                            colors = RadioButtonDefaults.colors(selectedColor = NexoraPurple)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = reason,
                            color = if (selectedReason == reason) Color.White else NexoraTextSecondary,
                            fontSize = 14.sp
                        )
                    }
                }

                if (onBlockUser != null) {
                    HorizontalDivider(
                        color = NexoraGlassBorder,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    OutlinedButton(
                        onClick = {
                            onBlockUser()
                            onDismiss()
                        },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = NexoraHeartRed),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Block this user")
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onSubmitReport(selectedReason) },
                colors = ButtonDefaults.buttonColors(containerColor = NexoraPurple)
            ) {
                Text("Submit Report", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = NexoraTextSecondary)
            }
        }
    )
}

@Composable
fun FirebaseSetupDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = NexoraSurfaceDark,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CloudDone,
                    contentDescription = null,
                    tint = NexoraNeonCyan
                )
                Text(
                    text = "Firebase Backend Setup",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Nexora is pre-engineered for seamless Google Firebase integration. Follow these 4 quick steps:",
                    color = NexoraTextSecondary,
                    fontSize = 13.sp
                )
                GlassBox(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    backgroundColor = NexoraSurfaceLightDark
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "1. Firebase Console: Create a project at console.firebase.google.com",
                            color = NexoraTextPrimary,
                            fontSize = 12.sp
                        )
                        Text(
                            text = "2. Add Android App with package: com.nexora.social",
                            color = NexoraNeonCyan,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "3. Download google-services.json and place it into /app/ folder",
                            color = NexoraTextPrimary,
                            fontSize = 12.sp
                        )
                        Text(
                            text = "4. Enable Authentication (Email & Google Sign-In) and Firestore Database.",
                            color = NexoraTextPrimary,
                            fontSize = 12.sp
                        )
                    }
                }
                Text(
                    text = "💡 In the meantime, Demo Mode allows 100% full interaction with feeds, reels, stories, comments, DMs, and profile editing directly on your phone!",
                    color = NexoraPinkAccent,
                    fontSize = 12.sp
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = NexoraPurple)
            ) {
                Text("Got It", color = Color.White)
            }
        }
    )
}
