package com.andriybobchuk.cocreate.feature.messages.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andriybobchuk.cocreate.core.data.repository.CoreRepository
import com.andriybobchuk.cocreate.core.domain.model.AuthorPost
import com.andriybobchuk.cocreate.feature.auth.data.repository.MessengerRepository
import com.andriybobchuk.cocreate.feature.feed.presentation.FeedState
import com.andriybobchuk.cocreate.feature.messages.domain.model.FullConversation
import com.andriybobchuk.cocreate.feature.messages.domain.model.Message
import com.andriybobchuk.cocreate.feature.profile.domain.model.ProfileData
import com.andriybobchuk.cocreate.util.toEpochMillis
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConversationViewModel @Inject constructor(
    private val repository: MessengerRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val searchConversations = SearchConversationsStrategy()

    private val items = savedStateHandle.getStateFlow("conversations", emptyList<FullConversation>())
    private val searchText = savedStateHandle.getStateFlow("searchText", "")
    private val isSearchActive = savedStateHandle.getStateFlow("isSearchActive", false)

    val state = combine(items, searchText, isSearchActive) { items, searchText, isSearchActive ->
        ConversationsState(
            items = searchConversations.execute(items, searchText),
            searchText = searchText,
            isSearchActive = isSearchActive,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ConversationsState())


//    var conversationList = listOf<FullConversation>()
//    var state = mutableStateOf(conversationList)

//    init {
//        loadItems()
//    }

//    private fun getAllConversations() {
//        viewModelScope.launch {
//            val userUid = repository.getCurrentUserID()
//            val partialConversations = repository.getConversationsByUserId(userUid)
//
//            val conversationsWithRecipients = partialConversations
//                .filter { conversation -> conversation.lastMessageId.isNotBlank() }
//                .map { conversation ->
//                val recipientUid = conversation.participants.find { it != repository.getCurrentUserID() }
//                val recipientProfileData = recipientUid?.let {
//                    repository.getProfileDataById(it)
//                }
//                val lastMessageData = conversation.lastMessageId.let { lastMessageId ->
//                    repository.getMessageById(lastMessageId)
//                }
//                FullConversation(
//                    recipientData = recipientProfileData!!,
//                    lastMessage = lastMessageData,
//                    convoData = conversation
//                )
//            }
//            state.value = conversationsWithRecipients
//        }
//    }

    fun loadItems() {
        viewModelScope.launch {
            val userUid = repository.getCurrentUserID()
            val partialConversations = repository.getConversationsByUserId(userUid)

            val conversationsWithRecipients = partialConversations
                .filter { conversation -> conversation.lastMessageId.isNotBlank() }
                .map { conversation ->
                    val recipientUid = conversation.participants.find { it != repository.getCurrentUserID() }
                    val recipientProfileData = recipientUid?.let {
                        repository.getProfileDataById(it)
                    }
                    val lastMessageData = conversation.lastMessageId.let { lastMessageId ->
                        repository.getMessageById(lastMessageId)
                    }
                    FullConversation(
                        recipientData = recipientProfileData!!,
                        lastMessage = lastMessageData,
                        convoData = conversation
                    )
                }
            savedStateHandle["conversations"] = conversationsWithRecipients
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

//    fun markConversationAsRead(conversationId: String) {
//        viewModelScope.launch {
//            repository.markConversationAsRead(conversationId)
//        }
//    }
//
//
//    fun markConversationUnread(conversationId: String) {
//        viewModelScope.launch {
//            repository.markConversationUnread(conversationId)
//        }
//    }





}


/**
 * TODO will have to implement SearchStartegy<T> like:
 * interface SearchStrategy<T> {
fun execute(items: List<T>, query: String): List<T>
}

 Then to use it I will be calling:

object SearchUtility {
fun <T> search(items: List<T>, query: String, searchStrategy: SearchStrategy<T>): List<T> {
return searchStrategy.execute(data, query)
}
}
 */
class SearchConversationsStrategy {
    fun execute(items: List<FullConversation>, query: String): List<FullConversation> {
        if(query.isBlank()) {
            return items.sortedByDescending {
                toEpochMillis(it.lastMessage.time)
            }
        }
        return items.filter {
            it.recipientData.name.trim().lowercase().contains(query.lowercase()) ||
                    it.recipientData.position.trim().lowercase().contains(query.lowercase()) ||
                    it.lastMessage.time.trim().lowercase().contains(query.lowercase()) ||
                    it.lastMessage.content.trim().lowercase().contains(query.lowercase())
        }.sortedByDescending {
            toEpochMillis(it.lastMessage.time)
        }
    }
}


// TODO Can implement ScreenState interface, then be called by interface
data class ConversationsState(
    val items: List<FullConversation> = emptyList(),
    val searchText: String = "",
    val isSearchActive: Boolean = false,
)