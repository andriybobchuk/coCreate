package com.andriybobchuk.cocreate.feature.messages.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andriybobchuk.cocreate.core.data.repository.CoreRepository
import com.andriybobchuk.cocreate.feature.messages.domain.model.Conversation
import com.andriybobchuk.cocreate.feature.messages.domain.model.FullConversation
import com.andriybobchuk.cocreate.feature.messages.domain.model.FullPrivateChat
import com.andriybobchuk.cocreate.feature.messages.domain.model.Message
import com.andriybobchuk.cocreate.util.ccLog
import com.andriybobchuk.cocreate.util.getCurrentDateTime
import com.andriybobchuk.cocreate.util.toEpochMillis
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrivateChatViewModel @Inject constructor(
    private val repository: CoreRepository
) : ViewModel() {

    var state = mutableStateOf(FullPrivateChat())

    fun send(convoId: String, content: String) {
        viewModelScope.launch {
            repository.addMessageToConversation(
                convoId,
                content,
                repository.getCurrentUserID(),
                getCurrentDateTime()
            )
        }
    }

    fun subscribeToRealtimeUpdates(chatId: String) {
        val currentUserUid = repository.getCurrentUserID()

        repository.getMessagesCollection()
            .whereEqualTo("convoId", chatId)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            firebaseFirestoreException?.let {
                return@addSnapshotListener
            }
            querySnapshot?.let {
                val messages = mutableListOf<Message>()
                for(messageDocument in it) {
                    val message = messageDocument.toObject(Message::class.java)
                    message.isMyMessage = (message.senderId == currentUserUid)
                    messages.add(message)
                }

                viewModelScope.launch {
                    // participants' IDs and the ID of last message
                    val chatData = repository.getConversationById(chatId)

                    val otherParticipantUid = chatData.participants.find { it != currentUserUid } ?: return@launch
                    val recipientProfileData = repository.getProfileDataById(otherParticipantUid)

                    val fullPrivateChat = FullPrivateChat(chatData, recipientProfileData, messages)

                    state.value = fullPrivateChat.sortByMessageTime()
                }
            }
        }
    }

    // Extension function to sort FullPrivateChat by message time
    private fun FullPrivateChat.sortByMessageTime(): FullPrivateChat {
        val sortedMessages = messages.sortedBy { toEpochMillis(it.time) }
        return copy(messages = sortedMessages)
    }
}