package com.andriybobchuk.cocreate.feature.feed.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andriybobchuk.cocreate.core.data.repository.CoreRepository
import com.andriybobchuk.cocreate.core.domain.model.AuthorPost
import com.andriybobchuk.cocreate.core.domain.model.Post
import com.andriybobchuk.cocreate.feature.auth.data.repository.PostRepository
import com.andriybobchuk.cocreate.feature.profile.domain.model.ProfileData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddPostViewModel @Inject constructor(
    private val repository: PostRepository
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
                    AuthorPost(post, authorProfile)
                } else {
                    AuthorPost(post, ProfileData())
                }
            }
            state.value = postsWithAuthorInfo
        }
    }

    fun addPost(title: String, desc: String, tags: List<String>) {
        viewModelScope.launch {
            repository.addNewPost(title, desc, tags)
        }
    }
}