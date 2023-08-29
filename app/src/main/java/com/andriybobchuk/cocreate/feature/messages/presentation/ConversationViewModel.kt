package com.andriybobchuk.cocreate.feature.messages.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andriybobchuk.cocreate.core.data.repository.CoreRepository
import com.andriybobchuk.cocreate.feature.messages.domain.model.FullConversation
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
        getAllConversations()
    }

    private fun getAllConversations() {
        viewModelScope.launch {
            val userUid = repository.getCurrentUserID()
            val partialConversations = repository.getConversationsByUserId(userUid)

            val conversationsWithRecipients = partialConversations.map { conversation ->
                val recipientUid = conversation.participants.find { it != repository.getCurrentUserID() }
                val recipientProfileData = recipientUid?.let {
                    repository.getProfileDataById(it)
                }
                val lastMessageData = conversation.lastMessageId?.let {
                    repository.getMessageById(it)
                }
                FullConversation(
                    recipientData = recipientProfileData!!,
                    lastMessage = lastMessageData!!,
                    convoData = conversation
                )
            }
            println("SIZE of recipientProfileData: " + partialConversations.size)

            state.value = conversationsWithRecipients
        }
    }

//    private fun getConversationList() {
//        viewModelScope.launch {
//            val convos = repository.getCurrentUserConversations()
//            for(convo in convos) {
//                conversationList += repository.getPersonByID(convo.)
//            }
//            state.value = friendsList
//        }
//    }
//
//    fun getAllConversations() {
//        viewModelScope.launch {
//            val userUid = getCurrentUserUid() // Implement this function to get the user's UID
//            if (userUid != null) {
//                val userConversations = repository.getConversationsByUserId(userUid)
//
//                val conversationsWithRecipients = userConversations.map { conversation ->
//                    val recipientUid = conversation.participants.find { it != userUid }
//                    val recipientProfileData = recipientUid?.let {
//                        repository.getRecipientProfileData(it)
//                    }
//                    ConversationWithRecipient(conversation, recipientProfileData)
//                }
//
//                _conversations.value = conversationsWithRecipients
//            }
//        }
//    }




}