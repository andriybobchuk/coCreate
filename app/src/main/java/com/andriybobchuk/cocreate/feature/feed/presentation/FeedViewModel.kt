package com.andriybobchuk.cocreate.feature.feed.presentation

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import androidx.navigation.NavController
import com.andriybobchuk.cocreate.core.data.repository.CoreRepository
import com.andriybobchuk.cocreate.core.domain.model.AuthorPost
import com.andriybobchuk.cocreate.core.domain.model.Post
import com.andriybobchuk.cocreate.feature.profile.domain.model.ProfileData
import com.andriybobchuk.cocreate.util.toEpochMillis
import com.andriybobchuk.navigation.Screens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val repository: CoreRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val searchPosts = SearchPosts()

    private val posts = savedStateHandle.getStateFlow("posts", emptyList<AuthorPost>())
    private val searchText = savedStateHandle.getStateFlow("searchText", "")
    private val isSearchActive = savedStateHandle.getStateFlow("isSearchActive", false)

    val state = combine(posts, searchText, isSearchActive) { posts, searchText, isSearchActive ->
        FeedState(
            posts = searchPosts.execute(posts, searchText),
            searchText = searchText,
            isSearchActive = isSearchActive,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), FeedState())

    fun loadPosts() {
        viewModelScope.launch {
            val allPosts = repository.getAllPosts()
            val postsWithAuthorInfo = allPosts.map { post ->
                if (post.author.isNotEmpty()) {
                    val authorProfile = repository.getProfileDataById(post.author)
//                    post.isLiked = repository.isPostLikedByCurrentUser(post.uid)
//                    post.likes = repository.getLikeCountForPost(post.uid)
                    AuthorPost(post, authorProfile)
                } else {
                    AuthorPost(post, ProfileData())
                }
            }
            savedStateHandle["posts"] = postsWithAuthorInfo
            savedStateHandle["isLoading"] = false
        }
    }

    fun onSearchTextChange(text: String) {
        savedStateHandle["searchText"] = text
    }

    fun onToggleSearch() {
        savedStateHandle["isSearchActive"] = !isSearchActive.value
        if(!isSearchActive.value) {
            savedStateHandle["searchText"] = ""
        }
    }

    fun navigateToProfileOrDetail(
        navController: NavController,
        userIdToNavigate: String,
    ) {
        if (repository.getCurrentUserID() == userIdToNavigate) {
            // Navigate to the user's own profile
            navController.navigate(Screens.ProfileScreen.route)
        } else {
            // Navigate to the detail screen for another user
            navController.navigate(
                "detail/{user}"
                    .replace(
                        oldValue = "{user}",
                        newValue = userIdToNavigate,
                    ),
            )
        }
    }

//    init {
//        getAllPosts()
//    }


//    fun likeOrUnlikePost(postId: String) {
//        viewModelScope.launch {
//            val isLiked = repository.isPostLikedByCurrentUser(postId)
//
//            if (isLiked) {
//                // Unlike the post
//                repository.unlikePost(postId)
//                state.value.find { it.postBody.uid == postId }?.apply {
//                    postBody.isLiked = false
//                    postBody.likes -= 1
//                }
//            } else {
//                // Like the post
//                repository.likePost(postId)
//                state.value.find { it.postBody.uid == postId }?.apply {
//                    postBody.isLiked = true
//                    postBody.likes += 1
//                }
//            }
//        }
//    }




//    fun likePost(postId: String) {
//        repository.likePost(postId)
//    }

}

/**
 * This is a use case for searching posts by a criteria
 */
class SearchPosts {
    fun execute(notes: List<AuthorPost>, query: String): List<AuthorPost> {
        if(query.isBlank()) {
            return notes.sortedByDescending {
                toEpochMillis(it.postBody.published)
            }
        }
        return notes.filter {
            it.postBody.title.trim().lowercase().contains(query.lowercase()) ||
                    it.postBody.desc.trim().lowercase().contains(query.lowercase()) ||
                    it.postBody.tags.any { tag -> tag.trim().lowercase().contains(query.lowercase()) } ||
                    it.postAuthor.name.trim().lowercase().contains(query.lowercase()) ||
                    it.postAuthor.city.trim().lowercase().contains(query.lowercase())
        }.sortedByDescending {
            toEpochMillis(it.postBody.published)
        }
    }
}

data class FeedState(
    val posts: List<AuthorPost> = emptyList(),
    val searchText: String = "",
    val isSearchActive: Boolean = false,
)