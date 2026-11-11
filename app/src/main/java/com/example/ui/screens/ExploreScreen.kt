package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TrendingUp
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
import com.example.data.model.Post
import com.example.ui.components.GlassBox
import com.example.ui.theme.*

@Composable
fun ExploreScreen(
    posts: List<Post>,
    searchQuery: String,
    selectedCategory: String,
    onSearchChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit,
    onPostClick: (Post) -> Unit
) {
    val categories = listOf("All", "Trending", "Tech", "Cyberpunk", "Design", "Gaming", "Photography", "Sound")

    val trendingTopics = listOf(
        Pair("#NexoraLaunch", "18.4K posts"),
        Pair("#SpatialUI", "12.1K posts"),
        Pair("#Android15", "9.8K posts"),
        Pair("#CyberpunkArt", "24.6K posts")
    )

    val filteredPosts = remember(posts, searchQuery, selectedCategory) {
        posts.filter { post ->
            val matchesQuery = searchQuery.isBlank() ||
                    post.caption.contains(searchQuery, ignoreCase = true) ||
                    post.user.username.contains(searchQuery, ignoreCase = true) ||
                    post.tags.any { it.contains(searchQuery, ignoreCase = true) }

            val matchesCategory = selectedCategory == "All" || selectedCategory == "Trending" ||
                    post.tags.any { it.equals(selectedCategory, ignoreCase = true) }

            matchesQuery && matchesCategory
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("explore_screen")
    ) {
        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            placeholder = { Text("Search creators, tags, topics...", color = NexoraTextMuted, fontSize = 14.sp) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = NexoraNeonCyan) },
            trailingIcon = {
                if (searchQuery.isNotBlank()) {
                    IconButton(onClick = { onSearchChange("") }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear", tint = NexoraTextMuted)
                    }
                }
            },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = NexoraPurple,
                unfocusedBorderColor = NexoraGlassBorder,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = NexoraSurfaceDark,
                unfocusedContainerColor = NexoraSurfaceDark
            ),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .testTag("explore_search_field")
        )

        // Category Pills
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories) { cat ->
                val isSelected = cat == selectedCategory
                Surface(
                    onClick = { onCategoryChange(cat) },
                    shape = RoundedCornerShape(16.dp),
                    color = if (isSelected) NexoraPurple else NexoraSurfaceLightDark,
                    border = if (!isSelected) ButtonDefaults.outlinedButtonBorder.copy(brush = Brush.linearGradient(listOf(NexoraGlassBorder, NexoraGlassBorder))) else null,
                    modifier = Modifier.testTag("category_pill_$cat")
                ) {
                    Text(
                        text = cat,
                        color = if (isSelected) Color.White else NexoraTextSecondary,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp),
            contentPadding = PaddingValues(bottom = 90.dp)
        ) {
            // Trending Highlights Section
            if (searchQuery.isBlank()) {
                item {
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.TrendingUp,
                            contentDescription = null,
                            tint = NexoraNeonCyan,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Trending on Nexora",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        trendingTopics.take(2).forEach { (tag, count) ->
                            GlassBox(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { onSearchChange(tag.removePrefix("#")) },
                                shape = RoundedCornerShape(14.dp),
                                backgroundColor = NexoraGlassSurface
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(text = tag, color = NexoraNeonCyan, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Text(text = count, color = NexoraTextMuted, fontSize = 11.sp)
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                }
            }

            // Grid of Posts
            item {
                Text(
                    text = "Explore Creations",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            val chunked = filteredPosts.chunked(2)
            items(chunked) { pair ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    pair.forEach { post ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(0.9f)
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    Brush.linearGradient(
                                        listOf(
                                            Color(post.placeholderGradientStart),
                                            Color(post.placeholderGradientEnd)
                                        )
                                    )
                                )
                                .clickable { onPostClick(post) }
                        ) {
                            if (post.imageUrl.isNotBlank()) {
                                AsyncImage(
                                    model = post.imageUrl,
                                    contentDescription = post.caption,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            // Bottom Gradient Overlay with stats
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .fillMaxWidth()
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f))
                                        )
                                    )
                                    .padding(8.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Favorite,
                                        contentDescription = null,
                                        tint = NexoraHeartRed,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = "${post.likesCount}",
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                    if (pair.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}
