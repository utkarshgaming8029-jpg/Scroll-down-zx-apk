package com.example.data.model

data class User(
    val id: String,
    val username: String,
    val displayName: String,
    val avatarUrl: String,
    val bio: String = "",
    val website: String = "",
    val followersCount: Int = 0,
    val followingCount: Int = 0,
    val postsCount: Int = 0,
    val isVerified: Boolean = false,
    val isFollowing: Boolean = false,
    val isBlocked: Boolean = false
)

data class StoryItem(
    val id: String,
    val mediaGradientStart: Long = 0xFF7928CA,
    val mediaGradientEnd: Long = 0xFF00DFD8,
    val caption: String = "",
    val timestamp: String = "2h ago"
)

data class Story(
    val id: String,
    val user: User,
    val items: List<StoryItem> = emptyList(),
    val hasUnseen: Boolean = true
)

data class Comment(
    val id: String,
    val postId: String,
    val user: User,
    val content: String,
    val timestamp: String,
    val likesCount: Int = 0,
    val isLiked: Boolean = false
)

data class Post(
    val id: String,
    val user: User,
    val imageUrl: String = "",
    val placeholderGradientStart: Long = 0xFF4A00E0,
    val placeholderGradientEnd: Long = 0xFF8E2DE2,
    val caption: String,
    val tags: List<String> = emptyList(),
    val location: String = "",
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val sharesCount: Int = 0,
    val isLiked: Boolean = false,
    val isSaved: Boolean = false,
    val timestamp: String = "Just now",
    val comments: List<Comment> = emptyList()
)

data class Reel(
    val id: String,
    val user: User,
    val caption: String,
    val musicTrack: String = "Nexora Original Sound",
    val gradientStart: Long = 0xFF2E0854,
    val gradientEnd: Long = 0xFF0B2545,
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val sharesCount: Int = 0,
    val isLiked: Boolean = false,
    val isSaved: Boolean = false
)

data class ChatMessage(
    val id: String,
    val senderId: String,
    val text: String,
    val timestamp: String,
    val isMine: Boolean
)

data class Conversation(
    val id: String,
    val otherUser: User,
    val lastMessage: String,
    val lastTimestamp: String,
    val unreadCount: Int = 0,
    val messages: List<ChatMessage> = emptyList()
)

enum class NotificationType {
    LIKE,
    COMMENT,
    FOLLOW,
    MENTION
}

data class NotificationItem(
    val id: String,
    val type: NotificationType,
    val user: User,
    val content: String,
    val timestamp: String,
    val isRead: Boolean = false
)

data class ReportItem(
    val id: String,
    val targetId: String,
    val targetType: String, // "POST" or "USER"
    val reason: String,
    val timestamp: String
)
