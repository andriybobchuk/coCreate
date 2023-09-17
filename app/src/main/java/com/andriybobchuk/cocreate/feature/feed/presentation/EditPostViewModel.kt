package com.andriybobchuk.cocreate.feature.feed.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andriybobchuk.cocreate.core.data.repository.CoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PostDetailState(
    val title: String = "",
    val desc: String = "",
    val tags: List<String> = listOf()
)

@HiltViewModel
class EditPostViewModel @Inject constructor(
    private val repository: CoreRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val title = savedStateHandle.getStateFlow("title", "")
    private val desc = savedStateHandle.getStateFlow("desc", "")
    private val tags = savedStateHandle.getStateFlow("tags", emptyList<String>())

    val state = combine(
        title,
        desc,
        tags
    ) { title, desc, tags ->
        PostDetailState(
            title = title,
            desc = desc,
            tags = tags
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PostDetailState())

    private val _hasPostBeenSaved = MutableStateFlow(false)
    val hasPostBeenSaved = _hasPostBeenSaved.asStateFlow()

    private var existingPostId: String = ""

    init {
        savedStateHandle.get<String>("id")?.let { existingPostId ->
            this.existingPostId = existingPostId
            viewModelScope.launch {
                getPostById(existingPostId)
            }
        }
    }

    fun getPostById(id: String) {
        viewModelScope.launch {
            val postBody = repository.getPostDataById(id)
            savedStateHandle["title"] = postBody.title
            savedStateHandle["desc"] = postBody.desc
            savedStateHandle["tags"] = postBody.tags
        }
    }

    fun onTitleChanged(text: String) {
        savedStateHandle["title"] = text
    }

    fun onDescChanged(text: String) {
        savedStateHandle["desc"] = text
    }

    fun onTagsChanged(tags: List<String>) {
        savedStateHandle["tags"] = tags
    }

    fun updatePost() {
        viewModelScope.launch {
            repository.updatePost(existingPostId, title.value, desc.value, tags.value)
            _hasPostBeenSaved.value = true
        }
    }

    fun deletePost() {
        viewModelScope.launch {
            repository.deletePost(existingPostId)
        }
    }
}


