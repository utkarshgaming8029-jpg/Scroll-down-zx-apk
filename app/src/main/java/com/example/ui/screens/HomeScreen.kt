package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.spring
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
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.Post
import com.example.data.model.Story
import com.example.data.model.User
import com.example.ui.components.DemoModeBanner
import com.example.ui.components.GlassBox
import com.example.ui.components.UserAvatar
import com.example.ui.components.VerifiedBadge
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    posts: List<Post>,
    stories: List<Story>,
    currentUser: User,
    isDemoMode: Boolean,
    onStoryClick: (Story) -> Unit,
    onLikePost: (String) -> Unit,
    onSavePost: (String) -> Unit,
    onOpenComments: (Post) -> Unit,
    onFollowUser: (String) -> Unit,
    onReportPost: (String) -> Unit,
    onBlockUser: (String) -> Unit,
    onShowFirebaseInfo: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("home_feed_list"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Demo Mode Banner
        if (isDemoMode) {
            item {
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    DemoModeBanner(onInfoClick = onShowFirebaseInfo)
                }
            }
        }

        // Stories Tray
        item {
            StoriesTray(
                stories = stories,
                currentUser = currentUser,
                onStoryClick = onStoryClick
            )
            HorizontalDivider(
                color = NexoraGlassBorder.copy(alpha = 0.5f),
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        // Feed Posts
        items(posts, key = { it.id }) { post ->
            PostCard(
                post = post,
                currentUserId = currentUser.id,
                onLike = { onLikePost(post.id) },
                onSave = { onSavePost(post.id) },
                onComment = { onOpenComments(post) },
                onFollow = { onFollowUser(post.user.id) },
                onReport = { onReportPost(post.id) },
                onBlock = { onBlockUser(post.user.id) }
            )
            Spacer(modifier = Modifier.height(14.dp))
        }
    }
}

@Composable
private fun StoriesTray(
    stories: List<Story>,
    currentUser: User,
    onStoryClick: (Story) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Your Story Item
        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable {
                    val myStory = stories.find { it.user.id == currentUser.id }
                    if (myStory != null) onStoryClick(myStory)
                }
            ) {
                Box(contentAlignment = Alignment.BottomEnd) {
                    UserAvatar(
                        imageUrl = currentUser.avatarUrl,
                        username = currentUser.username,
                        size = 64.dp,
                        hasStory = true,
                        hasUnseenStory = false
                    )
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(NexoraNeonCyan)
                            .border(2.dp, NexoraDeepDark, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add story",
                            tint = Color.Black,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Your Story",
                    color = NexoraTextSecondary,
                    fontSize = 11.sp,
                    maxLines = 1
                )
            }
        }

        // Other Users' Stories
        items(stories.filter { it.user.id != currentUser.id }, key = { it.id }) { story ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable { onStoryClick(story) }
            ) {
                UserAvatar(
                    imageUrl = story.user.avatarUrl,
                    username = story.user.username,
                    size = 64.dp,
                    hasStory = true,
                    hasUnseenStory = story.hasUnseen
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = story.user.username,
                    color = Color.White,
                    fontSize = 11.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.widthIn(max = 68.dp)
                )
            }
        }
    }
}

@Composable
fun PostCard(
    post: Post,
    currentUserId: String,
    onLike: () -> Unit,
    onSave: () -> Unit,
    onComment: () -> Unit,
    onFollow: () -> Unit,
    onReport: () -> Unit,
    onBlock: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    GlassBox(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .testTag("post_card_${post.id}"),
        shape = RoundedCornerShape(18.dp),
        backgroundColor = NexoraGlassSurface,
        borderColor = NexoraGlassBorder
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Post Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    UserAvatar(
                        imageUrl = post.user.avatarUrl,
                        username = post.user.username,
                        size = 40.dp
                    )
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = post.user.username,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            if (post.user.isVerified) {
                                VerifiedBadge(size = 14.dp)
                            }
                            if (post.user.id != currentUserId && !post.user.isFollowing) {
                                Text(
                                    text = "• Follow",
                                    color = NexoraNeonCyan,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .clickable { onFollow() }
                                        .padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                        if (post.location.isNotBlank()) {
                            Text(
                                text = post.location,
                                color = NexoraTextMuted,
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                Box {
                    IconButton(
                        onClick = { showMenu = true },
                        modifier = Modifier.testTag("post_menu_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Post options",
                            tint = NexoraTextSecondary
                        )
                    }

                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false },
                        containerColor = NexoraSurfaceDark
                    ) {
                        DropdownMenuItem(
                            text = { Text("Report Post", color = NexoraHeartRed) },
                            onClick = {
                                showMenu = false
                                onReport()
                            },
                            leadingIcon = {
                                Icon(Icons.Default.Report, contentDescription = null, tint = NexoraHeartRed)
                            }
                        )
                        if (post.user.id != currentUserId) {
                            DropdownMenuItem(
                                text = { Text("Block @${post.user.username}", color = NexoraTextSecondary) },
                                onClick = {
                                    showMenu = false
                                    onBlock()
                                },
                                leadingIcon = {
                                    Icon(Icons.Default.Block, contentDescription = null, tint = NexoraTextSecondary)
                                }
                            )
                        }
                    }
                }
            }

            // Post Media Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.15f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(
                                Color(post.placeholderGradientStart),
                                Color(post.placeholderGradientEnd)
                            )
                        )
                    )
            ) {
                if (post.imageUrl.isNotBlank()) {
                    AsyncImage(
                        model = post.imageUrl,
                        contentDescription = "Post image by ${post.user.username}",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            // Interactive Actions Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Like Button
                    IconButton(
                        onClick = onLike,
                        modifier = Modifier.testTag("like_post_button_${post.id}")
                    ) {
                        Icon(
                            imageVector = if (post.isLiked) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Like",
                            tint = if (post.isLiked) NexoraHeartRed else Color.White
                        )
                    }

                    // Comment Button
                    IconButton(
                        onClick = onComment,
                        modifier = Modifier.testTag("comment_post_button_${post.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ChatBubbleOutline,
                            contentDescription = "Comments",
                            tint = Color.White
                        )
                    }

                    // Share Button
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Outlined.Send,
                            contentDescription = "Share",
                            tint = Color.White
                        )
                    }
                }

                // Bookmark Button
                IconButton(
                    onClick = onSave,
                    modifier = Modifier.testTag("save_post_button_${post.id}")
                ) {
                    Icon(
                        imageVector = if (post.isSaved) Icons.Default.Bookmark else Icons.Outlined.BookmarkBorder,
                        contentDescription = "Save post",
                        tint = if (post.isSaved) NexoraNeonCyan else Color.White
                    )
                }
            }

            // Likes Counter & Details
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "${post.likesCount} likes",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Caption
                Text(
                    text = "${post.user.username} ${post.caption}",
                    color = NexoraTextPrimary,
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                // Tags
                if (post.tags.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = post.tags.joinToString(" ") { "#$it" },
                        color = NexoraPurple,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // View comments prompt
                if (post.commentsCount > 0) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "View all ${post.commentsCount} comments",
                        color = NexoraTextMuted,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .clickable { onComment() }
                            .padding(vertical = 2.dp)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = post.timestamp,
                    color = NexoraTextMuted,
                    fontSize = 10.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}
