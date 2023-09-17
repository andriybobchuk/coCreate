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
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    private val repository: CoreRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val comments = savedStateHandle.getStateFlow("comments", emptyList<AuthorComment>())
    private val searchText = savedStateHandle.getStateFlow("searchText", "")
//    var comments = listOf<AuthorComment>()
//    var commentsState = mutableStateOf(comments)

    //val state = comments.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), State())
    val state = combine(comments, searchText) { comments, searchText ->
        State(
            comments = comments
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), State())

    val postDataState = mutableStateOf(AuthorPost())

    private val _hasCommentBeenAdded = MutableStateFlow(false)
    val hasCommentBeenAdded = _hasCommentBeenAdded.asStateFlow()

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

            val commentsWithAuthorInfo = comments.map { comment ->
                if (comment.author.isNotEmpty()) {
                    val authorProfile = repository.getProfileDataById(comment.author)
                    AuthorComment(comment, authorProfile)
                } else {
                    AuthorComment(comment, ProfileData())
                }
            }
            //commentsState.value = postsWithAuthorInfo
            savedStateHandle["comments"] = commentsWithAuthorInfo
        }
    }

    fun addComment(postUid: String, desc: String) {
        viewModelScope.launch {
            repository.addComment(postUid, desc)
            _hasCommentBeenAdded.value = true
        }
    }

    fun removeComment(id: String) {
        viewModelScope.launch {
            repository.removeComment(id)
        }
    }
}

data class State(
    val comments: List<AuthorComment> = emptyList(),
)