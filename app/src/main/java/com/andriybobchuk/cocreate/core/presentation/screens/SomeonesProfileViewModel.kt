package com.andriybobchuk.cocreate.core.presentation.screens

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.andriybobchuk.cocreate.core.data.repository.CoreRepository
import com.andriybobchuk.cocreate.feature.auth.data.repository.ContactsRepository
import com.andriybobchuk.cocreate.feature.auth.data.repository.MessengerRepository
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
    private val coreRepository: CoreRepository,
    private val contactsRepository: ContactsRepository,
    private val messengerRepository: MessengerRepository,
) : ViewModel() {
    val state = mutableStateOf(ProfileData())

    fun getProfileDataById(id: String) {
        viewModelScope.launch {
            state.value = coreRepository.getProfileDataById(id)
        }
    }

    suspend fun isUserSavedAsContact(): Boolean {
        return coreRepository.getProfileDataById(coreRepository.getCurrentUserID()).contacts.contains(state.value.uid)
    }
    fun addContact() {
        viewModelScope.launch {
            contactsRepository.addContact(state.value.uid)
        }
    }
    fun removeContact() {
        viewModelScope.launch {
            contactsRepository.removeContact(state.value.uid)
        }
    }

    fun sendOrOpenExistingConversation(userId: String, navController: NavController) {
        viewModelScope.launch {
            val existingConversationId = messengerRepository.findExistingConversationWithUser(userId)

            if (existingConversationId != null) {
                // An existing conversation was found, navigate to it
                navController.navigate("privateChat/$existingConversationId")
            } else {
                // No existing conversation found, create a new one
                val newConversationId = messengerRepository.createNewConversation(userId)
                navController.navigate("privateChat/$newConversationId")
            }
        }
    }
}