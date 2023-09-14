package com.andriybobchuk.cocreate.core.presentation.screens

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andriybobchuk.cocreate.core.data.repository.CoreRepository
import com.andriybobchuk.cocreate.feature.profile.data.repository.ProfileRepository
import com.andriybobchuk.cocreate.feature.profile.domain.model.ProfileData
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SomeonesProfileViewModel @Inject constructor(
    private val repository: CoreRepository
) : ViewModel() {
    val state = mutableStateOf(ProfileData())

    fun getProfileDataById(id: String) {
        viewModelScope.launch {
            state.value = repository.getProfileDataById(id)
        }
    }

    // ViewModel function to check if a user is a friend
    suspend fun isUserAFriend(userId: String): Boolean {
        return repository.isFriend(userId)
    }

    fun removeFriend(friendUid: String) {
        viewModelScope.launch {
            repository.removeFriend(friendUid)
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

    // ViewModel function to check if a user sent a friend request
    suspend fun didUserSendFriendRequest(userId: String): Boolean {
        return repository.didUserSendFriendRequest(userId)
    }
}