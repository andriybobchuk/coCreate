package com.andriybobchuk.cocreate.feature.feed.presentation

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andriybobchuk.cocreate.core.data.repository.CoreRepository
import com.andriybobchuk.cocreate.core.domain.model.AuthorPost
import com.andriybobchuk.cocreate.core.domain.model.Post
import com.andriybobchuk.cocreate.feature.profile.domain.model.ProfileData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val repository: CoreRepository
) : ViewModel() {

    var posts = listOf<AuthorPost>()
    var state = mutableStateOf(posts)

    init {
        getAllPosts()
    }

    private fun getAllPosts() {
        viewModelScope.launch {
            val allPosts = repository.getAllPosts()

            val postsWithAuthorInfo = allPosts.map { post ->
                if (post.author.isNotEmpty()) {
                    val authorProfile = repository.getProfileDataById(post.author)
                    post.isLiked = repository.isPostLikedByCurrentUser(post.uid)
                    post.likes = repository.getLikeCountForPost(post.uid)
                    AuthorPost(post, authorProfile)
                } else {
                    AuthorPost(post, ProfileData())
                }
            }
            state.value = postsWithAuthorInfo
        }
    }

    fun likeOrUnlikePost(postId: String) {
        viewModelScope.launch {
            val isLiked = repository.isPostLikedByCurrentUser(postId)

            if (isLiked) {
                // Unlike the post
                repository.unlikePost(postId)
                state.value.find { it.postBody.uid == postId }?.apply {
                    postBody.isLiked = false
                    postBody.likes -= 1
                }
            } else {
                // Like the post
                repository.likePost(postId)
                state.value.find { it.postBody.uid == postId }?.apply {
                    postBody.isLiked = true
                    postBody.likes += 1
                }
            }
        }
    }




//    fun likePost(postId: String) {
//        repository.likePost(postId)
//    }
}