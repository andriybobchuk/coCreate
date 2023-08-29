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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrivateChatViewModel @Inject constructor(
    private val repository: CoreRepository
) : ViewModel() {

    var state = mutableStateOf(FullPrivateChat())

    fun getPrivateChatDataByChatId(id: String) {
        ccLog.d("PrivateChatViewModel", "ChatId = $id")
        viewModelScope.launch {
            val chatData = repository.getConversationById(id)
            val currentUserUid = repository.getCurrentUserID()
            val otherParticipantUid = chatData.participants.find { it != currentUserUid } ?: return@launch
            ccLog.d("PrivateChatViewModel", "otherParticipantUid = $otherParticipantUid")
            val recipientProfileData = repository.getProfileDataById(otherParticipantUid)
            ccLog.d("PrivateChatViewModel", "recipientProfileData = $recipientProfileData")
            val messages = repository.getMessagesForChat(id)
            ccLog.d("PrivateChatViewModel", "messages.size = ${messages.size}")
            val fullPrivateChat = FullPrivateChat(chatData, recipientProfileData, messages)

            state.value = fullPrivateChat
        }
    }

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
}