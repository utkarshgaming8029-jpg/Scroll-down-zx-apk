package com.example.data.repository

import com.example.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NexoraRepository {

    // Current authenticated user (Demo user by default)
    private val _currentUser = MutableStateFlow(
        User(
            id = "me_user_1",
            username = "utkarsh_nex",
            displayName = "Utkarsh Gaming",
            avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=400&q=80",
            bio = "Crafting the next generation of social connectivity 🚀 | Creator & Tech Enthusiast | #Nexora",
            website = "https://nexora.social",
            followersCount = 1420,
            followingCount = 389,
            postsCount = 18,
            isVerified = true
        )
    )
    val currentUser: StateFlow<User> = _currentUser.asStateFlow()

    private val _isDemoMode = MutableStateFlow(true)
    val isDemoMode: StateFlow<Boolean> = _isDemoMode.asStateFlow()

    private val _isAuthenticated = MutableStateFlow(true)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated.asStateFlow()

    // Blocked users set
    private val _blockedUserIds = MutableStateFlow<Set<String>>(emptySet())
    val blockedUserIds: StateFlow<Set<String>> = _blockedUserIds.asStateFlow()

    // Reports list
    private val _reports = MutableStateFlow<List<ReportItem>>(emptyList())
    val reports: StateFlow<List<ReportItem>> = _reports.asStateFlow()

    // Initial Users
    private val sampleUsers = listOf(
        User(
            id = "user_elena",
            username = "elena_vfx",
            displayName = "Elena Rostova",
            avatarUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=400&q=80",
            bio = "3D motion designer & spatial computing artist. Tokyo 🇯🇵",
            followersCount = 8940,
            followingCount = 412,
            postsCount = 64,
            isVerified = true,
            isFollowing = true
        ),
        User(
            id = "user_marcus",
            username = "marcus_cyber",
            displayName = "Marcus Chen",
            avatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=400&q=80",
            bio = "AI safety researcher & neural interface hacker.",
            followersCount = 12500,
            followingCount = 280,
            postsCount = 92,
            isVerified = true,
            isFollowing = false
        ),
        User(
            id = "user_maya",
            username = "maya_visuals",
            displayName = "Maya Al-Mansoor",
            avatarUrl = "https://images.unsplash.com/photo-1517841905240-472988babdf9?auto=format&fit=crop&w=400&q=80",
            bio = "Cyberpunk street photography | Leica & Lumix",
            followersCount = 5430,
            followingCount = 620,
            postsCount = 47,
            isVerified = false,
            isFollowing = true
        ),
        User(
            id = "user_zack",
            username = "zack_sound",
            displayName = "Zack Rivera",
            avatarUrl = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&w=400&q=80",
            bio = "Synthwave & ambient audio producer 🎧 New EP out now!",
            followersCount = 3120,
            followingCount = 195,
            postsCount = 31,
            isVerified = false,
            isFollowing = false
        )
    )

    // Stories
    private val _stories = MutableStateFlow<List<Story>>(
        listOf(
            Story(
                id = "story_me",
                user = _currentUser.value,
                items = listOf(
                    StoryItem(
                        id = "si_0",
                        mediaGradientStart = 0xFF6A11CB,
                        mediaGradientEnd = 0xFF2575FC,
                        caption = "Live demo from Android Emulator! Testing Nexora ⚡",
                        timestamp = "Just now"
                    )
                ),
                hasUnseen = false
            ),
            Story(
                id = "story_elena",
                user = sampleUsers[0],
                items = listOf(
                    StoryItem(
                        id = "si_1",
                        mediaGradientStart = 0xFFFF0844,
                        mediaGradientEnd = 0xFFFFB199,
                        caption = "Late night render in Blender 4.2 ✨",
                        timestamp = "1h ago"
                    ),
                    StoryItem(
                        id = "si_2",
                        mediaGradientStart = 0xFF4E65FF,
                        mediaGradientEnd = 0xFF92EFFD,
                        caption = "Neon reflections in Shibuya alleyways 🌧️",
                        timestamp = "3h ago"
                    )
                ),
                hasUnseen = true
            ),
            Story(
                id = "story_marcus",
                user = sampleUsers[1],
                items = listOf(
                    StoryItem(
                        id = "si_3",
                        mediaGradientStart = 0xFF00C6FF,
                        mediaGradientEnd = 0xFF0072FF,
                        caption = "Benchmarking new local edge neural models ⚡",
                        timestamp = "4h ago"
                    )
                ),
                hasUnseen = true
            ),
            Story(
                id = "story_maya",
                user = sampleUsers[2],
                items = listOf(
                    StoryItem(
                        id = "si_4",
                        mediaGradientStart = 0xFFF857A6,
                        mediaGradientEnd = 0xFFFF5858,
                        caption = "Sunset at the digital art summit 🌅",
                        timestamp = "6h ago"
                    )
                ),
                hasUnseen = true
            ),
            Story(
                id = "story_zack",
                user = sampleUsers[3],
                items = listOf(
                    StoryItem(
                        id = "si_5",
                        mediaGradientStart = 0xFF11998E,
                        mediaGradientEnd = 0xFF38EF7D,
                        caption = "Synth modular session live stream in 10 mins 🎹",
                        timestamp = "7h ago"
                    )
                ),
                hasUnseen = false
            )
        )
    )
    val stories: StateFlow<List<Story>> = _stories.asStateFlow()

    // Posts Feed
    private val _posts = MutableStateFlow<List<Post>>(
        listOf(
            Post(
                id = "post_1",
                user = sampleUsers[0],
                imageUrl = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?auto=format&fit=crop&w=900&q=80",
                placeholderGradientStart = 0xFF8A2BE2,
                placeholderGradientEnd = 0xFF4A00E0,
                caption = "Immersive holographic interfaces are becoming reality sooner than we think. What do you think about spatial social spaces? 🌌✨",
                tags = listOf("Cyberpunk", "Design", "VFX", "FutureTech"),
                location = "Tokyo, Japan",
                likesCount = 384,
                commentsCount = 28,
                sharesCount = 14,
                isLiked = false,
                isSaved = true,
                timestamp = "2 hours ago",
                comments = listOf(
                    Comment(
                        id = "c_1",
                        postId = "post_1",
                        user = sampleUsers[1],
                        content = "The lighting dynamics here are incredible! Outstanding work.",
                        timestamp = "1h ago",
                        likesCount = 12,
                        isLiked = true
                    ),
                    Comment(
                        id = "c_2",
                        postId = "post_1",
                        user = _currentUser.value,
                        content = "Connect beyond limits! Love this vibe 🚀",
                        timestamp = "30m ago",
                        likesCount = 5,
                        isLiked = false
                    )
                )
            ),
            Post(
                id = "post_2",
                user = sampleUsers[1],
                imageUrl = "https://images.unsplash.com/photo-1526374965328-7f61d4dc18c5?auto=format&fit=crop&w=900&q=80",
                placeholderGradientStart = 0xFF0052D4,
                placeholderGradientEnd = 0xFF4364F7,
                caption = "Exploring decentralized cryptography on mobile devices. Fast, secure, and private by default. 🔒💻",
                tags = listOf("Crypto", "Security", "Android", "Dev"),
                location = "San Francisco, CA",
                likesCount = 621,
                commentsCount = 45,
                sharesCount = 33,
                isLiked = true,
                isSaved = false,
                timestamp = "5 hours ago",
                comments = listOf(
                    Comment(
                        id = "c_3",
                        postId = "post_2",
                        user = sampleUsers[2],
                        content = "Privacy first is the future of social apps 👏",
                        timestamp = "3h ago",
                        likesCount = 8,
                        isLiked = false
                    )
                )
            ),
            Post(
                id = "post_3",
                user = sampleUsers[2],
                imageUrl = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?auto=format&fit=crop&w=900&q=80",
                placeholderGradientStart = 0xFFFF416C,
                placeholderGradientEnd = 0xFFFF4B2B,
                caption = "Neon rain reflections on the streets of Neo-Seoul. Captured with 50mm f/1.2 📸🌧️",
                tags = listOf("StreetPhotography", "Neon", "Cyberpunk", "Art"),
                location = "Seoul, South Korea",
                likesCount = 1204,
                commentsCount = 87,
                sharesCount = 59,
                isLiked = false,
                isSaved = false,
                timestamp = "8 hours ago",
                comments = emptyList()
            )
        )
    )
    val posts: StateFlow<List<Post>> = _posts.asStateFlow()

    // Reels Feed
    private val _reels = MutableStateFlow<List<Reel>>(
        listOf(
            Reel(
                id = "reel_1",
                user = sampleUsers[0],
                caption = "Creating neon shader particles in Jetpack Compose Canvas! 💫 #AndroidDev #Design",
                musicTrack = "Zack Rivera – Cyber Odyssey (Original Mix)",
                gradientStart = 0xFF3A1C71,
                gradientEnd = 0xFFD76D77,
                likesCount = 2410,
                commentsCount = 142,
                sharesCount = 98,
                isLiked = true,
                isSaved = true
            ),
            Reel(
                id = "reel_2",
                user = sampleUsers[2],
                caption = "Midnight hyperlapse through the skyscrapers 🏙️⚡ Experience the velocity.",
                musicTrack = "Lofi Dreams – Neo Tokyo 2077",
                gradientStart = 0xFF0F2027,
                gradientEnd = 0xFF2C5364,
                likesCount = 5820,
                commentsCount = 310,
                sharesCount = 220,
                isLiked = false,
                isSaved = false
            ),
            Reel(
                id = "reel_3",
                user = sampleUsers[3],
                caption = "Analog synthesis tutorial: generating fat basslines with Moog oscillators 🎛️",
                musicTrack = "Studio Session #42 – Live Jam",
                gradientStart = 0xFF141E30,
                gradientEnd = 0xFF243B55,
                likesCount = 1890,
                commentsCount = 89,
                sharesCount = 44,
                isLiked = false,
                isSaved = false
            )
        )
    )
    val reels: StateFlow<List<Reel>> = _reels.asStateFlow()

    // Conversations & Messages
    private val _conversations = MutableStateFlow<List<Conversation>>(
        listOf(
            Conversation(
                id = "conv_1",
                otherUser = sampleUsers[0],
                lastMessage = "Thanks for the feedback on the spatial UI! Let's collab soon.",
                lastTimestamp = "14:25",
                unreadCount = 1,
                messages = listOf(
                    ChatMessage("m_1", "user_elena", "Hey Utkarsh! Did you check out the new 3D motion prototype?", "14:20", isMine = false),
                    ChatMessage("m_2", "me_user_1", "Yes! The glassmorphism and motion curves look pristine 🚀", "14:22", isMine = true),
                    ChatMessage("m_3", "user_elena", "Thanks for the feedback on the spatial UI! Let's collab soon.", "14:25", isMine = false)
                )
            ),
            Conversation(
                id = "conv_2",
                otherUser = sampleUsers[1],
                lastMessage = "I'll push the end-to-end security audit today.",
                lastTimestamp = "Yesterday",
                unreadCount = 0,
                messages = listOf(
                    ChatMessage("m_4", "me_user_1", "Hey Marcus, is the crypto integration ready?", "Yesterday", isMine = true),
                    ChatMessage("m_5", "user_marcus", "I'll push the end-to-end security audit today.", "Yesterday", isMine = false)
                )
            ),
            Conversation(
                id = "conv_3",
                otherUser = sampleUsers[2],
                lastMessage = "Sent you the high-res wallpaper pack!",
                lastTimestamp = "2d ago",
                unreadCount = 0,
                messages = listOf(
                    ChatMessage("m_6", "user_maya", "Sent you the high-res wallpaper pack!", "2d ago", isMine = false)
                )
            )
        )
    )
    val conversations: StateFlow<List<Conversation>> = _conversations.asStateFlow()

    // Notifications
    private val _notifications = MutableStateFlow<List<NotificationItem>>(
        listOf(
            NotificationItem(
                id = "notif_1",
                type = NotificationType.LIKE,
                user = sampleUsers[0],
                content = "liked your post 'Testing Nexora on Android'",
                timestamp = "5m ago",
                isRead = false
            ),
            NotificationItem(
                id = "notif_2",
                type = NotificationType.COMMENT,
                user = sampleUsers[1],
                content = "commented: 'Connect beyond limits! Love this vibe 🚀'",
                timestamp = "32m ago",
                isRead = false
            ),
            NotificationItem(
                id = "notif_3",
                type = NotificationType.FOLLOW,
                user = sampleUsers[2],
                content = "started following you",
                timestamp = "2h ago",
                isRead = true
            ),
            NotificationItem(
                id = "notif_4",
                type = NotificationType.MENTION,
                user = sampleUsers[3],
                content = "mentioned you in a reel caption: '@utkarsh_nex check this track'",
                timestamp = "5h ago",
                isRead = true
            )
        )
    )
    val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

    // Actions

    fun toggleLikePost(postId: String) {
        _posts.update { list ->
            list.map { post ->
                if (post.id == postId) {
                    val newLiked = !post.isLiked
                    val newCount = if (newLiked) post.likesCount + 1 else post.likesCount - 1
                    post.copy(isLiked = newLiked, likesCount = maxOf(0, newCount))
                } else post
            }
        }
    }

    fun toggleSavePost(postId: String) {
        _posts.update { list ->
            list.map { post ->
                if (post.id == postId) post.copy(isSaved = !post.isSaved) else post
            }
        }
    }

    fun addComment(postId: String, content: String) {
        if (content.isBlank()) return
        val newComment = Comment(
            id = "c_${System.currentTimeMillis()}",
            postId = postId,
            user = _currentUser.value,
            content = content.trim(),
            timestamp = "Just now",
            likesCount = 0,
            isLiked = false
        )
        _posts.update { list ->
            list.map { post ->
                if (post.id == postId) {
                    post.copy(
                        commentsCount = post.commentsCount + 1,
                        comments = post.comments + newComment
                    )
                } else post
            }
        }
    }

    fun toggleLikeReel(reelId: String) {
        _reels.update { list ->
            list.map { reel ->
                if (reel.id == reelId) {
                    val newLiked = !reel.isLiked
                    val newCount = if (newLiked) reel.likesCount + 1 else reel.likesCount - 1
                    reel.copy(isLiked = newLiked, likesCount = maxOf(0, newCount))
                } else reel
            }
        }
    }

    fun toggleSaveReel(reelId: String) {
        _reels.update { list ->
            list.map { reel ->
                if (reel.id == reelId) reel.copy(isSaved = !reel.isSaved) else reel
            }
        }
    }

    fun toggleFollowUser(userId: String) {
        _posts.update { list ->
            list.map { post ->
                if (post.user.id == userId) {
                    val newFollowing = !post.user.isFollowing
                    val newFollowers = if (newFollowing) post.user.followersCount + 1 else post.user.followersCount - 1
                    post.copy(user = post.user.copy(isFollowing = newFollowing, followersCount = maxOf(0, newFollowers)))
                } else post
            }
        }
    }

    fun createPost(caption: String, tags: List<String>, location: String, imageUrl: String = "") {
        val newPost = Post(
            id = "post_${System.currentTimeMillis()}",
            user = _currentUser.value,
            imageUrl = imageUrl.ifBlank { "https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=900&q=80" },
            placeholderGradientStart = 0xFF7928CA,
            placeholderGradientEnd = 0xFFFF0080,
            caption = caption,
            tags = tags,
            location = location,
            likesCount = 1,
            commentsCount = 0,
            sharesCount = 0,
            isLiked = true,
            isSaved = false,
            timestamp = "Just now"
        )
        _posts.update { listOf(newPost) + it }
        _currentUser.update { it.copy(postsCount = it.postsCount + 1) }
    }

    fun sendMessage(conversationId: String, text: String) {
        if (text.isBlank()) return
        val newMsg = ChatMessage(
            id = "m_${System.currentTimeMillis()}",
            senderId = _currentUser.value.id,
            text = text.trim(),
            timestamp = "Just now",
            isMine = true
        )
        _conversations.update { list ->
            list.map { conv ->
                if (conv.id == conversationId) {
                    conv.copy(
                        lastMessage = newMsg.text,
                        lastTimestamp = "Just now",
                        messages = conv.messages + newMsg
                    )
                } else conv
            }
        }
    }

    fun startConversationWithUser(user: User): Conversation {
        val existing = _conversations.value.find { it.otherUser.id == user.id }
        if (existing != null) return existing

        val newConv = Conversation(
            id = "conv_${System.currentTimeMillis()}",
            otherUser = user,
            lastMessage = "Started a conversation",
            lastTimestamp = "Just now",
            unreadCount = 0,
            messages = emptyList()
        )
        _conversations.update { listOf(newConv) + it }
        return newConv
    }

    fun markStorySeen(storyId: String) {
        _stories.update { list ->
            list.map { if (it.id == storyId) it.copy(hasUnseen = false) else it }
        }
    }

    fun blockUser(userId: String) {
        _blockedUserIds.update { it + userId }
        // Filter out posts and stories from blocked user
        _posts.update { it.filter { post -> post.user.id != userId } }
        _stories.update { it.filter { story -> story.user.id != userId } }
        _conversations.update { it.filter { conv -> conv.otherUser.id != userId } }
    }

    fun unblockUser(userId: String) {
        _blockedUserIds.update { it - userId }
    }

    fun reportItem(targetId: String, targetType: String, reason: String) {
        val report = ReportItem(
            id = "rep_${System.currentTimeMillis()}",
            targetId = targetId,
            targetType = targetType,
            reason = reason,
            timestamp = "Just now"
        )
        _reports.update { listOf(report) + it }
    }

    fun markAllNotificationsRead() {
        _notifications.update { list -> list.map { it.copy(isRead = true) } }
    }

    fun updateProfile(displayName: String, bio: String, website: String) {
        _currentUser.update {
            it.copy(
                displayName = displayName.ifBlank { it.displayName },
                bio = bio,
                website = website
            )
        }
    }

    fun setDemoMode(enabled: Boolean) {
        _isDemoMode.value = enabled
    }

    fun logout() {
        _isAuthenticated.value = false
    }

    fun login(email: String, isDemo: Boolean = true) {
        _isAuthenticated.value = true
        _isDemoMode.value = isDemo
        if (!isDemo && email.isNotBlank()) {
            val handle = email.substringBefore("@")
            _currentUser.update {
                it.copy(
                    username = handle,
                    displayName = handle.replaceFirstChar { c -> c.uppercase() }
                )
            }
        }
    }
}
