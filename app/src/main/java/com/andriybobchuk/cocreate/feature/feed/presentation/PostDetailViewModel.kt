package com.andriybobchuk.cocreate.feature.feed.presentation

import androidx.compose.runtime.mutableStateOf
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
class PostDetailViewMOdel @Inject constructor(
    private val repository: CoreRepository
) : ViewModel() {

    var comments = listOf<AuthorComment>()
    var commentsState = mutableStateOf(comments)

    val postDataState = mutableStateOf(AuthorPost())

    fun getPostById(id: String) {
        viewModelScope.launch {
            val postBody = repository.getPostDataById(id)
            val authorProfile = repository.getProfileDataById(postBody.author)

            postDataState.value = AuthorPost(postBody, authorProfile)
        }
    }

    fun getCommentsByPostId(id: String) {
        viewModelScope.launch {
            val comments = repository.getCommentsByPostId(id)

            val postsWithAuthorInfo = comments.map { comment ->
                if (comment.author.isNotEmpty()) {
                    val authorProfile = repository.getProfileDataById(comment.author)
                    AuthorComment(comment, authorProfile)
                } else {
                    AuthorComment(comment, ProfileData())
                }
            }
            commentsState.value = postsWithAuthorInfo
        }
    }
}