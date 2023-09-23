package com.andriybobchuk.cocreate.feature.collaborators.presentation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andriybobchuk.cocreate.core.data.repository.CoreRepository
import com.andriybobchuk.cocreate.core.domain.model.AuthorPost
import com.andriybobchuk.cocreate.core.domain.model.Person
import com.andriybobchuk.cocreate.feature.feed.presentation.FeedState
import com.andriybobchuk.cocreate.feature.feed.presentation.SearchPosts
import com.andriybobchuk.cocreate.feature.profile.domain.model.ProfileData
import com.andriybobchuk.cocreate.util.ccLog
import com.andriybobchuk.cocreate.util.toEpochMillis
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollaboratorsViewModel @Inject constructor(
    private val repository: CoreRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    companion object {
        const val LOG_TAG = "CollaboratorsViewModel"
    }

    private val searchContacts = SearchContacts()

    private val everyone = savedStateHandle.getStateFlow("everyone", emptyList<ProfileData>())
    private val contacts = savedStateHandle.getStateFlow("contacts", emptyList<ProfileData>())
    private val searchText = savedStateHandle.getStateFlow("searchText", "")
    private val isSearchActive = savedStateHandle.getStateFlow("isSearchActive", false)

    val state = combine(everyone, contacts, searchText, isSearchActive) { everyone, contacts, searchText, isSearchActive ->
        CollaboratorsState(
            everyone = everyone,
            contacts = searchContacts.execute(everyone, contacts, searchText),
            searchText = searchText,
            isSearchActive = isSearchActive
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CollaboratorsState())

    fun getEveryone() {
        viewModelScope.launch {
            val everyone = repository.getAllPeople()
            savedStateHandle["everyone"] = everyone
            ccLog.d(LOG_TAG, "getEveryone(), size = " + everyone.size)
        }
    }
    fun getContacts() {
        viewModelScope.launch {
            val contacts = repository.getContacts()
            savedStateHandle["contacts"] = contacts
            ccLog.d(LOG_TAG, "getContacts(), size = " + contacts.size)
        }
    }
    fun addContact(contactUid: String) {
        viewModelScope.launch {
            repository.addContact(contactUid)
        }
    }
    fun removeContact(contactUid: String) {
        viewModelScope.launch {
            repository.removeContact(contactUid)
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
}

/**
 * This is a use case for searching collaborators by a criteria
 */
class SearchContacts {
    fun execute(everyone: List<ProfileData>, people: List<ProfileData>, query: String): List<ProfileData> {
        if(query.isBlank()) {
            return people
        }
        return everyone.filter {
            it.name.trim().lowercase().contains(query.lowercase()) ||
                    it.city.trim().lowercase().contains(query.lowercase()) ||
                    it.position.trim().lowercase().contains(query.lowercase()) ||
                    it.tags.any { tag -> tag.trim().lowercase().contains(query.lowercase()) } ||
                    it.email.trim().lowercase().contains(query.lowercase())
        }
    }
}

data class CollaboratorsState(
    val everyone: List<ProfileData> = emptyList(),
    val contacts: List<ProfileData> = emptyList(),
    val searchText: String = "",
    val isSearchActive: Boolean = false
)