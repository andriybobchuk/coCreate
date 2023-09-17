package com.andriybobchuk.cocreate.feature.feed.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andriybobchuk.cocreate.core.data.repository.CoreRepository
import com.andriybobchuk.cocreate.core.domain.model.AuthorComment
import com.andriybobchuk.cocreate.core.domain.model.AuthorPost
import com.andriybobchuk.cocreate.feature.profile.domain.model.ProfileData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditPostViewModel @Inject constructor(
    private val repository: CoreRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    var postDataState = mutableStateOf(AuthorPost())

    fun updateTitle(title: String) {
        postDataState.value.postBody.title = title
    }

    fun getPostById(id: String) {
        viewModelScope.launch {
            val postBody = repository.getPostDataById(id)
            val authorProfile = repository.getProfileDataById(postBody.author)

            postDataState.value = AuthorPost(postBody, authorProfile)
        }
    }

    fun updatePost(id: String, title: String, desc: String, tags: List<String>) {
        viewModelScope.launch {
            repository.updatePost(id, title, desc, tags)
        }
    }

    fun deletePost(id: String) {
        viewModelScope.launch {
            repository.deletePost(id)
        }
    }

//    fun getCommentsByPostId(id: String) {
//        viewModelScope.launch {
//            val comments = repository.getCommentsByPostId(id)
//
//            val postsWithAuthorInfo = comments.map { comment ->
//                if (comment.author.isNotEmpty()) {
//                    val authorProfile = repository.getProfileDataById(comment.author)
//                    AuthorComment(comment, authorProfile)
//                } else {
//                    AuthorComment(comment, ProfileData())
//                }
//            }
//            commentsState.value = postsWithAuthorInfo
//        }
//    }
}


