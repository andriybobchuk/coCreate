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

    var requestList = listOf<ProfileData>()
    var requestState = mutableStateOf(requestList)

    init {
        getFriendsList()
        getExploreList()
        getRequestList()
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

    private fun getRequestList() {
        viewModelScope.launch {
            val requests = repository.getRequestorUids()
            for(request in requests) {
                requestList += repository.getPersonByID(request)
            }
            requestState.value = requestList
        }
    }

    private fun getExploreList() {
        viewModelScope.launch {
            val people = repository.getNewPeopleIds()
            for(person in people) {
                exploreList += repository.getPersonByID(person)
            }
            exploreState.value = exploreList
        }
    }

    fun requestFriend(requestedUid: String) {
        viewModelScope.launch {
            repository.requestFriend(requestedUid)
        }
    }

    fun approveFriend(requestorUid: String) {
        viewModelScope.launch {
            repository.approveFriend(requestorUid)
        }
    }



//    fun isFriend(profileUid: String): Boolean {
//        viewModelScope.launch {
//            return repository.isFriend(profileUid)
//        }
//    }
}