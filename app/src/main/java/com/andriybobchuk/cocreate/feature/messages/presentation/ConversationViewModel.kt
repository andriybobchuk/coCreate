package com.andriybobchuk.cocreate.feature.messages.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andriybobchuk.cocreate.core.data.repository.CoreRepository
import com.andriybobchuk.cocreate.feature.messages.domain.model.Conversation
import com.andriybobchuk.cocreate.feature.messages.domain.model.FullConversation
import com.andriybobchuk.cocreate.feature.profile.domain.model.ProfileData
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConversationViewModel @Inject constructor(
    private val repository: CoreRepository,
) : ViewModel() {

    var conversationList = listOf<FullConversation>()
    var state = mutableStateOf(conversationList)

    init {
        getConversationList()
    }

    private fun getConversationList() {
        viewModelScope.launch {
            val convos = repository.getCurrentUserConversations()
            for(convo in convos) {
                conversationList += repository.getPersonByID(convo.)
            }
            state.value = friendsList
        }
    }
}