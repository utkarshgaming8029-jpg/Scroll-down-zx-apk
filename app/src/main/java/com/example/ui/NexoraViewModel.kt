package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.*
import com.example.data.repository.NexoraRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class Screen {
    SPLASH,
    AUTH,
    HOME,
    EXPLORE,
    CREATE,
    REELS,
    PROFILE,
    MESSAGES,
    CHAT,
    NOTIFICATIONS,
    STORY_VIEWER,
    SETTINGS
}

class NexoraViewModel(
    val repository: NexoraRepository = NexoraRepository()
) : ViewModel() {

    private val _currentScreen = MutableStateFlow(Screen.SPLASH)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val screenBackStack = mutableListOf<Screen>()

    // Selected items for modal / detail views
    private val _selectedStory = MutableStateFlow<Story?>(null)
    val selectedStory: StateFlow<Story?> = _selectedStory.asStateFlow()

    private val _selectedConversation = MutableStateFlow<Conversation?>(null)
    val selectedConversation: StateFlow<Conversation?> = _selectedConversation.asStateFlow()

    private val _activeCommentsPost = MutableStateFlow<Post?>(null)
    val activeCommentsPost: StateFlow<Post?> = _activeCommentsPost.asStateFlow()

    private val _activeReportTarget = MutableStateFlow<Pair<String, String>?>(null) // id to "POST" or "USER"
    val activeReportTarget: StateFlow<Pair<String, String>?> = _activeReportTarget.asStateFlow()

    private val _showEditProfileDialog = MutableStateFlow(false)
    val showEditProfileDialog: StateFlow<Boolean> = _showEditProfileDialog.asStateFlow()

    private val _showFirebaseDialog = MutableStateFlow(false)
    val showFirebaseDialog: StateFlow<Boolean> = _showFirebaseDialog.asStateFlow()

    // Explore filter & search
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    // Notifications filter
    private val _notificationsFilter = MutableStateFlow("All")
    val notificationsFilter: StateFlow<String> = _notificationsFilter.asStateFlow()

    // Observed repository flows
    val currentUser = repository.currentUser
    val isDemoMode = repository.isDemoMode
    val isAuthenticated = repository.isAuthenticated
    val posts = repository.posts
    val stories = repository.stories
    val reels = repository.reels
    val conversations = repository.conversations
    val notifications = repository.notifications
    val blockedUserIds = repository.blockedUserIds
    val reports = repository.reports

    init {
        // Observe auth changes
        viewModelScope.launch {
            isAuthenticated.collect { authed ->
                if (!authed && _currentScreen.value != Screen.SPLASH) {
                    _currentScreen.value = Screen.AUTH
                }
            }
        }
    }

    fun navigateTo(screen: Screen) {
        if (_currentScreen.value != screen) {
            screenBackStack.add(_currentScreen.value)
            _currentScreen.value = screen
        }
    }

    fun handleBack(): Boolean {
        if (_activeCommentsPost.value != null) {
            _activeCommentsPost.value = null
            return true
        }
        if (_activeReportTarget.value != null) {
            _activeReportTarget.value = null
            return true
        }
        if (_showEditProfileDialog.value) {
            _showEditProfileDialog.value = false
            return true
        }
        if (_showFirebaseDialog.value) {
            _showFirebaseDialog.value = false
            return true
        }
        if (_currentScreen.value == Screen.STORY_VIEWER) {
            _selectedStory.value = null
            _currentScreen.value = Screen.HOME
            return true
        }
        if (_currentScreen.value == Screen.CHAT) {
            _selectedConversation.value = null
            _currentScreen.value = Screen.MESSAGES
            return true
        }
        if (screenBackStack.isNotEmpty()) {
            val prev = screenBackStack.removeAt(screenBackStack.lastIndex)
            _currentScreen.value = prev
            return true
        }
        return false
    }

    // Story
    fun openStory(story: Story) {
        _selectedStory.value = story
        repository.markStorySeen(story.id)
        navigateTo(Screen.STORY_VIEWER)
    }

    fun closeStory() {
        _selectedStory.value = null
        if (_currentScreen.value == Screen.STORY_VIEWER) {
            handleBack()
        }
    }

    // Direct Messages
    fun openConversation(conversation: Conversation) {
        _selectedConversation.value = conversation
        navigateTo(Screen.CHAT)
    }

    fun startChatWithUser(user: User) {
        val conv = repository.startConversationWithUser(user)
        _selectedConversation.value = conv
        navigateTo(Screen.CHAT)
    }

    fun sendMessage(text: String) {
        _selectedConversation.value?.let { conv ->
            repository.sendMessage(conv.id, text)
            // Refresh reference to latest conversation messages
            _selectedConversation.value = repository.conversations.value.find { it.id == conv.id }
        }
    }

    // Comments
    fun openComments(post: Post) {
        _activeCommentsPost.value = post
    }

    fun closeComments() {
        _activeCommentsPost.value = null
    }

    fun addComment(text: String) {
        _activeCommentsPost.value?.let { post ->
            repository.addComment(post.id, text)
            // Update active comments post reference
            _activeCommentsPost.value = repository.posts.value.find { it.id == post.id }
        }
    }

    // Post Interactions
    fun toggleLikePost(postId: String) {
        repository.toggleLikePost(postId)
    }

    fun toggleSavePost(postId: String) {
        repository.toggleSavePost(postId)
    }

    fun toggleFollowUser(userId: String) {
        repository.toggleFollowUser(userId)
    }

    fun createPost(caption: String, tags: List<String>, location: String, imageUrl: String) {
        repository.createPost(caption, tags, location, imageUrl)
        navigateTo(Screen.HOME)
    }

    // Reel Interactions
    fun toggleLikeReel(reelId: String) {
        repository.toggleLikeReel(reelId)
    }

    fun toggleSaveReel(reelId: String) {
        repository.toggleSaveReel(reelId)
    }

    // Explore
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedCategory(cat: String) {
        _selectedCategory.value = cat
    }

    // Notifications
    fun setNotificationsFilter(filter: String) {
        _notificationsFilter.value = filter
    }

    fun markAllNotificationsRead() {
        repository.markAllNotificationsRead()
    }

    // Moderation: Report & Block
    fun openReportDialog(targetId: String, targetType: String) {
        _activeReportTarget.value = Pair(targetId, targetType)
    }

    fun closeReportDialog() {
        _activeReportTarget.value = null
    }

    fun submitReport(reason: String) {
        _activeReportTarget.value?.let { (id, type) ->
            repository.reportItem(id, type, reason)
        }
        _activeReportTarget.value = null
    }

    fun blockUser(userId: String) {
        repository.blockUser(userId)
    }

    fun unblockUser(userId: String) {
        repository.unblockUser(userId)
    }

    // Profile & Settings
    fun setShowEditProfileDialog(show: Boolean) {
        _showEditProfileDialog.value = show
    }

    fun setShowFirebaseDialog(show: Boolean) {
        _showFirebaseDialog.value = show
    }

    fun updateProfile(displayName: String, bio: String, website: String) {
        repository.updateProfile(displayName, bio, website)
        _showEditProfileDialog.value = false
    }

    fun toggleDemoMode(enabled: Boolean) {
        repository.setDemoMode(enabled)
    }

    fun logout() {
        repository.logout()
        _currentScreen.value = Screen.AUTH
    }

    fun login(email: String, isDemo: Boolean) {
        repository.login(email, isDemo)
        _currentScreen.value = Screen.HOME
    }
}
