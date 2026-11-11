package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.NotificationItem
import com.example.data.model.NotificationType
import com.example.ui.components.GlassBox
import com.example.ui.components.UserAvatar
import com.example.ui.theme.*

@Composable
fun NotificationsScreen(
    notifications: List<NotificationItem>,
    selectedFilter: String,
    onFilterChange: (String) -> Unit,
    onMarkAllRead: () -> Unit,
    onBack: () -> Unit
) {
    val filters = listOf("All", "Follows", "Mentions")

    val filteredList = remember(notifications, selectedFilter) {
        when (selectedFilter) {
            "Follows" -> notifications.filter { it.type == NotificationType.FOLLOW }
            "Mentions" -> notifications.filter { it.type == NotificationType.MENTION }
            else -> notifications
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("notifications_screen")
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
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
                    text = "Activity",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            IconButton(onClick = onMarkAllRead) {
                Icon(
                    imageVector = Icons.Default.DoneAll,
                    contentDescription = "Mark all as read",
                    tint = NexoraNeonCyan
                )
            }
        }

        // Filter chips
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filters) { f ->
                val isSelected = f == selectedFilter
                Surface(
                    onClick = { onFilterChange(f) },
                    shape = RoundedCornerShape(16.dp),
                    color = if (isSelected) NexoraPurple else NexoraSurfaceLightDark,
                    border = if (!isSelected) ButtonDefaults.outlinedButtonBorder.copy(
                        brush = Brush.linearGradient(listOf(NexoraGlassBorder, NexoraGlassBorder))
                    ) else null
                ) {
                    Text(
                        text = f,
                        color = if (isSelected) Color.White else NexoraTextSecondary,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(color = NexoraGlassBorder)

        // Notifications List
        if (filteredList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No notifications in this category",
                    color = NexoraTextMuted,
                    fontSize = 14.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 20.dp)
            ) {
                items(filteredList, key = { it.id }) { item ->
                    NotificationRow(item = item)
                }
            }
        }
    }
}

@Composable
private fun NotificationRow(item: NotificationItem) {
    val (typeIcon, iconColor) = when (item.type) {
        NotificationType.LIKE -> Pair(Icons.Default.Favorite, NexoraHeartRed)
        NotificationType.COMMENT -> Pair(Icons.Default.ChatBubble, NexoraNeonCyan)
        NotificationType.FOLLOW -> Pair(Icons.Default.PersonAdd, NexoraPurple)
        NotificationType.MENTION -> Pair(Icons.Default.AlternateEmail, NexoraPinkAccent)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (!item.isRead) NexoraViolet.copy(alpha = 0.12f) else Color.Transparent)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(contentAlignment = Alignment.BottomEnd) {
            UserAvatar(
                imageUrl = item.user.avatarUrl,
                username = item.user.username,
                size = 46.dp
            )
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(iconColor)
                    .border(1.5.dp, NexoraDeepDark, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = typeIcon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(10.dp)
                )
            }
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "${item.user.username} ${item.content}",
                color = NexoraTextPrimary,
                fontSize = 13.sp,
                lineHeight = 17.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = item.timestamp,
                color = NexoraTextMuted,
                fontSize = 11.sp
            )
        }

        if (item.type == NotificationType.FOLLOW) {
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor = NexoraPurple),
                shape = RoundedCornerShape(14.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                modifier = Modifier.height(32.dp)
            ) {
                Text("Follow", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        } else if (!item.isRead) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(NexoraNeonCyan)
            )
        }
    }
}
