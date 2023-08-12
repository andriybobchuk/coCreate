package com.andriybobchuk.cocreate.feature.collaborators.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andriybobchuk.cocreate.core.data.repository.CoreRepository
import com.andriybobchuk.cocreate.core.domain.model.Person
import com.andriybobchuk.cocreate.feature.profile.domain.model.ProfileData
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollaboratorsViewModel @Inject constructor(
    private val repository: CoreRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    var friendsList = listOf<ProfileData>()
    var state = mutableStateOf(friendsList)

    var exploreList = listOf<ProfileData>()
    var exploreState = mutableStateOf(exploreList)

    init {
        getFriendsList()
        getExploreList()
    }

    private fun getFriendsList() {
        viewModelScope.launch {
            val friends = repository.getCurrentUserFriends()
            for(friend in friends) {
                friendsList += repository.getPersonByID(friend)
            }
            state.value = friendsList
        }
    }

    private fun getExploreList() {
        viewModelScope.launch {
            val people = repository.getAllPeople()
            exploreState.value = people
        }
    }
}