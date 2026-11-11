package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Public
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.User
import com.example.ui.components.GlassBox
import com.example.ui.components.GradientButton
import com.example.ui.components.UserAvatar
import com.example.ui.theme.*

@Composable
fun CreatePostScreen(
    currentUser: User,
    onPublishPost: (caption: String, tags: List<String>, location: String, imageUrl: String) -> Unit
) {
    var caption by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("Tokyo, Japan") }
    var selectedTagList by remember { mutableStateOf(setOf("Nexora", "Tech")) }

    val presetImages = listOf(
        Pair("Cyberpunk City", "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?auto=format&fit=crop&w=900&q=80"),
        Pair("Spatial Hologram", "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?auto=format&fit=crop&w=900&q=80"),
        Pair("Neon Aesthetic", "https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=900&q=80"),
        Pair("Night Drive", "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?auto=format&fit=crop&w=900&q=80")
    )
    var selectedImageUrl by remember { mutableStateOf(presetImages[0].second) }

    val availableTags = listOf("Nexora", "Tech", "Design", "Cyberpunk", "Art", "Android", "Gaming", "Photography")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .testTag("create_post_screen")
    ) {
        // User Info header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            UserAvatar(
                imageUrl = currentUser.avatarUrl,
                username = currentUser.username,
                size = 44.dp
            )
            Column {
                Text(
                    text = currentUser.displayName,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Public,
                        contentDescription = null,
                        tint = NexoraNeonCyan,
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = "Public Audience",
                        color = NexoraTextSecondary,
                        fontSize = 12.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Selected Media Preview
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(NexoraSurfaceLightDark)
                .border(1.dp, NexoraGlassBorder, RoundedCornerShape(18.dp)),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = selectedImageUrl,
                contentDescription = "Selected media preview",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Surface(
                color = Color.Black.copy(alpha = 0.5f),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Image,
                        contentDescription = null,
                        tint = NexoraNeonCyan,
                        modifier = Modifier.size(14.dp)
                    )
                    Text("Media Preview", color = Color.White, fontSize = 11.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Preset Media Choices
        Text(
            text = "Select Media Asset",
            color = NexoraTextSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(6.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(presetImages) { (label, url) ->
                val isSelected = selectedImageUrl == url
                GlassBox(
                    modifier = Modifier
                        .clickable { selectedImageUrl = url }
                        .then(
                            if (isSelected) Modifier.border(2.dp, NexoraNeonCyan, RoundedCornerShape(12.dp))
                            else Modifier
                        ),
                    shape = RoundedCornerShape(12.dp),
                    backgroundColor = if (isSelected) NexoraViolet.copy(alpha = 0.4f) else NexoraSurfaceLightDark
                ) {
                    Text(
                        text = label,
                        color = if (isSelected) Color.White else NexoraTextSecondary,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Caption Input
        OutlinedTextField(
            value = caption,
            onValueChange = { caption = it },
            placeholder = { Text("What are you thinking? Write a caption...", color = NexoraTextMuted) },
            minLines = 3,
            maxLines = 6,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = NexoraPurple,
                unfocusedBorderColor = NexoraGlassBorder,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = NexoraGlassSurface,
                unfocusedContainerColor = NexoraGlassSurface
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("caption_input_field")
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Tags Section
        Text(
            text = "Add Hashtags",
            color = NexoraTextSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(6.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(availableTags) { tag ->
                val isSelected = selectedTagList.contains(tag)
                Surface(
                    onClick = {
                        selectedTagList = if (isSelected) selectedTagList - tag else selectedTagList + tag
                    },
                    shape = RoundedCornerShape(16.dp),
                    color = if (isSelected) NexoraPurple else NexoraSurfaceLightDark,
                    border = if (!isSelected) ButtonDefaults.outlinedButtonBorder.copy(brush = Brush.linearGradient(listOf(NexoraGlassBorder, NexoraGlassBorder))) else null
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        if (isSelected) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                        }
                        Text(
                            text = "#$tag",
                            color = if (isSelected) Color.White else NexoraTextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Location Tagging
        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("Add Location", color = NexoraTextSecondary) },
            leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = NexoraNeonCyan) },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = NexoraPurple,
                unfocusedBorderColor = NexoraGlassBorder,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Publish Post Button
        GradientButton(
            text = "Share to Nexora Feed 🚀",
            onClick = {
                val finalCaption = caption.ifBlank { "Sharing from Nexora – Connect Beyond Limits ✨" }
                onPublishPost(finalCaption, selectedTagList.toList(), location, selectedImageUrl)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 90.dp),
            testTag = "publish_post_button"
        )
    }
}
