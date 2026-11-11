package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.R
import com.example.data.model.Post
import com.example.ui.components.*
import com.example.ui.screens.*
import com.example.ui.theme.*

@Composable
fun NexoraApp(
    viewModel: NexoraViewModel = viewModel()
) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val isDemoMode by viewModel.isDemoMode.collectAsStateWithLifecycle()
    val posts by viewModel.posts.collectAsStateWithLifecycle()
    val stories by viewModel.stories.collectAsStateWithLifecycle()
    val reels by viewModel.reels.collectAsStateWithLifecycle()
    val conversations by viewModel.conversations.collectAsStateWithLifecycle()
    val notifications by viewModel.notifications.collectAsStateWithLifecycle()
    val blockedUserIds by viewModel.blockedUserIds.collectAsStateWithLifecycle()
    val reports by viewModel.reports.collectAsStateWithLifecycle()

    val selectedStory by viewModel.selectedStory.collectAsStateWithLifecycle()
    val selectedConversation by viewModel.selectedConversation.collectAsStateWithLifecycle()
    val activeCommentsPost by viewModel.activeCommentsPost.collectAsStateWithLifecycle()
    val activeReportTarget by viewModel.activeReportTarget.collectAsStateWithLifecycle()
    val showEditProfileDialog by viewModel.showEditProfileDialog.collectAsStateWithLifecycle()
    val showFirebaseDialog by viewModel.showFirebaseDialog.collectAsStateWithLifecycle()

    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val notificationsFilter by viewModel.notificationsFilter.collectAsStateWithLifecycle()

    val unreadMessagesCount = conversations.sumOf { it.unreadCount }
    val unreadNotificationsCount = notifications.count { !it.isRead }

    // Intercept hardware Back Button
    BackHandler(enabled = currentScreen != Screen.HOME && currentScreen != Screen.SPLASH) {
        viewModel.handleBack()
    }

    // Modal Sheet: Comments
    if (activeCommentsPost != null) {
        CommentsBottomSheet(
            post = activeCommentsPost!!,
            currentUser = currentUser,
            onDismiss = { viewModel.closeComments() },
            onAddComment = { text -> viewModel.addComment(text) }
        )
    }

    // Modal Dialog: Report & Block
    if (activeReportTarget != null) {
        val (targetId, targetType) = activeReportTarget!!
        ReportBlockDialog(
            targetType = targetType,
            onDismiss = { viewModel.closeReportDialog() },
            onSubmitReport = { reason -> viewModel.submitReport(reason) },
            onBlockUser = { viewModel.blockUser(targetId) }
        )
    }

    // Modal Dialog: Firebase Setup
    if (showFirebaseDialog) {
        FirebaseSetupDialog(
            onDismiss = { viewModel.setShowFirebaseDialog(false) }
        )
    }

    // Modal Dialog: Edit Profile
    if (showEditProfileDialog) {
        EditProfileDialog(
            user = currentUser,
            onDismiss = { viewModel.setShowEditProfileDialog(false) },
            onSave = { name, bio, site -> viewModel.updateProfile(name, bio, site) }
        )
    }

    // Top-level Screen Router
    when (currentScreen) {
        Screen.SPLASH -> {
            SplashScreen(
                onSplashFinished = {
                    viewModel.navigateTo(Screen.HOME)
                }
            )
        }

        Screen.AUTH -> {
            AuthScreen(
                onLoginSuccess = { email, isDemo ->
                    viewModel.login(email, isDemo)
                },
                onShowFirebaseInfo = {
                    viewModel.setShowFirebaseDialog(true)
                }
            )
        }

        Screen.STORY_VIEWER -> {
            if (selectedStory != null) {
                StoryViewerScreen(
                    story = selectedStory!!,
                    onClose = { viewModel.closeStory() }
                )
            } else {
                viewModel.navigateTo(Screen.HOME)
            }
        }

        Screen.CHAT -> {
            if (selectedConversation != null) {
                ChatDetailScreen(
                    conversation = selectedConversation!!,
                    onSendMessage = { text -> viewModel.sendMessage(text) },
                    onBack = { viewModel.handleBack() }
                )
            } else {
                viewModel.navigateTo(Screen.MESSAGES)
            }
        }

        Screen.MESSAGES -> {
            Scaffold(
                containerColor = NexoraDeepDark,
                contentWindowInsets = WindowInsets.safeDrawing
            ) { innerPadding ->
                Box(modifier = Modifier.padding(innerPadding)) {
                    MessagesScreen(
                        conversations = conversations,
                        onSelectConversation = { conv -> viewModel.openConversation(conv) },
                        onBack = { viewModel.handleBack() }
                    )
                }
            }
        }

        Screen.NOTIFICATIONS -> {
            Scaffold(
                containerColor = NexoraDeepDark,
                contentWindowInsets = WindowInsets.safeDrawing
            ) { innerPadding ->
                Box(modifier = Modifier.padding(innerPadding)) {
                    NotificationsScreen(
                        notifications = notifications,
                        selectedFilter = notificationsFilter,
                        onFilterChange = { f -> viewModel.setNotificationsFilter(f) },
                        onMarkAllRead = { viewModel.markAllNotificationsRead() },
                        onBack = { viewModel.handleBack() }
                    )
                }
            }
        }

        Screen.SETTINGS -> {
            Scaffold(
                containerColor = NexoraDeepDark,
                contentWindowInsets = WindowInsets.safeDrawing
            ) { innerPadding ->
                Box(modifier = Modifier.padding(innerPadding)) {
                    SettingsScreen(
                        isDemoMode = isDemoMode,
                        blockedCount = blockedUserIds.size,
                        reportsCount = reports.size,
                        onToggleDemoMode = { enabled -> viewModel.toggleDemoMode(enabled) },
                        onShowFirebaseDialog = { viewModel.setShowFirebaseDialog(true) },
                        onLogout = { viewModel.logout() },
                        onBack = { viewModel.handleBack() }
                    )
                }
            }
        }

        else -> {
            // Main 5-tab application scaffold
            val showBottomBar = currentScreen in listOf(
                Screen.HOME,
                Screen.EXPLORE,
                Screen.CREATE,
                Screen.REELS,
                Screen.PROFILE
            )

            Scaffold(
                containerColor = NexoraDeepDark,
                contentWindowInsets = WindowInsets.safeDrawing,
                topBar = {
                    if (currentScreen == Screen.HOME) {
                        NexoraHomeTopBar(
                            unreadMessages = unreadMessagesCount,
                            unreadNotifications = unreadNotificationsCount,
                            onMessagesClick = { viewModel.navigateTo(Screen.MESSAGES) },
                            onNotificationsClick = { viewModel.navigateTo(Screen.NOTIFICATIONS) }
                        )
                    } else if (currentScreen == Screen.CREATE) {
                        NexoraSectionTopBar(title = "Create Post")
                    } else if (currentScreen == Screen.PROFILE) {
                        NexoraSectionTopBar(title = "@${currentUser.username}")
                    }
                },
                bottomBar = {
                    if (showBottomBar) {
                        NexoraBottomNavBar(
                            currentScreen = currentScreen,
                            currentUserAvatar = currentUser.avatarUrl,
                            currentUsername = currentUser.username,
                            onTabSelected = { target ->
                                viewModel.navigateTo(target)
                            }
                        )
                    }
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    when (currentScreen) {
                        Screen.HOME -> {
                            HomeScreen(
                                posts = posts,
                                stories = stories,
                                currentUser = currentUser,
                                isDemoMode = isDemoMode,
                                onStoryClick = { story -> viewModel.openStory(story) },
                                onLikePost = { id -> viewModel.toggleLikePost(id) },
                                onSavePost = { id -> viewModel.toggleSavePost(id) },
                                onOpenComments = { post -> viewModel.openComments(post) },
                                onFollowUser = { userId -> viewModel.toggleFollowUser(userId) },
                                onReportPost = { postId -> viewModel.openReportDialog(postId, "POST") },
                                onBlockUser = { userId -> viewModel.blockUser(userId) },
                                onShowFirebaseInfo = { viewModel.setShowFirebaseDialog(true) }
                            )
                        }

                        Screen.EXPLORE -> {
                            ExploreScreen(
                                posts = posts,
                                searchQuery = searchQuery,
                                selectedCategory = selectedCategory,
                                onSearchChange = { q -> viewModel.setSearchQuery(q) },
                                onCategoryChange = { c -> viewModel.setSelectedCategory(c) },
                                onPostClick = { post -> viewModel.openComments(post) }
                            )
                        }

                        Screen.CREATE -> {
                            CreatePostScreen(
                                currentUser = currentUser,
                                onPublishPost = { cap, tags, loc, img ->
                                    viewModel.createPost(cap, tags, loc, img)
                                }
                            )
                        }

                        Screen.REELS -> {
                            ReelsScreen(
                                reels = reels,
                                onLikeReel = { id -> viewModel.toggleLikeReel(id) },
                                onSaveReel = { id -> viewModel.toggleSaveReel(id) },
                                onCommentClick = {
                                    posts.firstOrNull()?.let { viewModel.openComments(it) }
                                }
                            )
                        }

                        Screen.PROFILE -> {
                            ProfileScreen(
                                user = currentUser,
                                posts = posts,
                                reels = reels,
                                onEditProfileClick = { viewModel.setShowEditProfileDialog(true) },
                                onSettingsClick = { viewModel.navigateTo(Screen.SETTINGS) },
                                onPostClick = { post -> viewModel.openComments(post) }
                            )
                        }

                        else -> {}
                    }
                }
            }
        }
    }
}

@Composable
private fun NexoraHomeTopBar(
    unreadMessages: Int,
    unreadNotifications: Int,
    onMessagesClick: () -> Unit,
    onNotificationsClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(NexoraDeepDark)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Brand Logo & Title
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_nexora_logo),
                contentDescription = "Nexora Logo",
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
            )
            Text(
                text = "NEXORA",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 2.sp
            )
        }

        // Action Icons (Notifications & Direct Messages)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Notifications Bell with Badge
            Box {
                IconButton(
                    onClick = onNotificationsClick,
                    modifier = Modifier.testTag("top_bar_notifications_button")
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = "Notifications",
                        tint = Color.White
                    )
                }
                if (unreadNotifications > 0) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 8.dp, end = 8.dp)
                            .size(9.dp)
                            .clip(CircleShape)
                            .background(NexoraHeartRed)
                    )
                }
            }

            // Direct Messages with Badge
            Box {
                IconButton(
                    onClick = onMessagesClick,
                    modifier = Modifier.testTag("top_bar_messages_button")
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Send,
                        contentDescription = "Direct Messages",
                        tint = Color.White
                    )
                }
                if (unreadMessages > 0) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 8.dp, end = 8.dp)
                            .size(9.dp)
                            .clip(CircleShape)
                            .background(NexoraNeonCyan)
                    )
                }
            }
        }
    }
}

@Composable
private fun NexoraSectionTopBar(title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(NexoraDeepDark)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
    }
}

@Composable
private fun NexoraBottomNavBar(
    currentScreen: Screen,
    currentUserAvatar: String,
    currentUsername: String,
    onTabSelected: (Screen) -> Unit
) {
    Surface(
        color = NexoraSurfaceDark,
        tonalElevation = 8.dp,
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. HOME
            IconButton(
                onClick = { onTabSelected(Screen.HOME) },
                modifier = Modifier.testTag("bottom_tab_home")
            ) {
                Icon(
                    imageVector = if (currentScreen == Screen.HOME) Icons.Filled.Home else Icons.Outlined.Home,
                    contentDescription = "Home",
                    tint = if (currentScreen == Screen.HOME) NexoraNeonCyan else NexoraTextSecondary
                )
            }

            // 2. EXPLORE
            IconButton(
                onClick = { onTabSelected(Screen.EXPLORE) },
                modifier = Modifier.testTag("bottom_tab_explore")
            ) {
                Icon(
                    imageVector = if (currentScreen == Screen.EXPLORE) Icons.Filled.Search else Icons.Outlined.Search,
                    contentDescription = "Explore",
                    tint = if (currentScreen == Screen.EXPLORE) NexoraNeonCyan else NexoraTextSecondary
                )
            }

            // 3. CREATE (Center Gradient Button)
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(NexoraPrimaryGradient)
                    .clickable { onTabSelected(Screen.CREATE) }
                    .testTag("bottom_tab_create"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Create Post",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            // 4. REELS
            IconButton(
                onClick = { onTabSelected(Screen.REELS) },
                modifier = Modifier.testTag("bottom_tab_reels")
            ) {
                Icon(
                    imageVector = if (currentScreen == Screen.REELS) Icons.Filled.PlayCircle else Icons.Outlined.PlayCircle,
                    contentDescription = "Reels",
                    tint = if (currentScreen == Screen.REELS) NexoraNeonCyan else NexoraTextSecondary
                )
            }

            // 5. PROFILE
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .border(
                        width = if (currentScreen == Screen.PROFILE) 2.dp else 1.dp,
                        color = if (currentScreen == Screen.PROFILE) NexoraNeonCyan else NexoraGlassBorder,
                        shape = CircleShape
                    )
                    .clickable { onTabSelected(Screen.PROFILE) }
                    .testTag("bottom_tab_profile"),
                contentAlignment = Alignment.Center
            ) {
                UserAvatar(
                    imageUrl = currentUserAvatar,
                    username = currentUsername,
                    size = 28.dp
                )
            }
        }
    }
}
