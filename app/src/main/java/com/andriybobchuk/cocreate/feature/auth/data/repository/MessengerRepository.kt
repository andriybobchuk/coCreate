package com.andriybobchuk.cocreate.feature.auth.data.repository

import com.andriybobchuk.cocreate.feature.messages.domain.model.Conversation
import com.andriybobchuk.cocreate.feature.messages.domain.model.Message
import com.andriybobchuk.cocreate.feature.profile.domain.model.ProfileData
import com.google.firebase.firestore.CollectionReference

interface MessengerRepository {

    // Core
    fun getCurrentUserID(): String
    suspend fun getAllPeople(): List<ProfileData>
    suspend fun getProfileDataById(id: String): ProfileData

    // Messenger
    fun getMessagesCollection(): CollectionReference
    suspend fun getConversationsByUserId(userId: String): List<Conversation>
    suspend fun getMessageById(id: String): Message
    suspend fun getConversationById(chatId: String): Conversation
    suspend fun addMessageToConversation( conversationId: String, content: String, senderId: String, time: String)
    suspend fun createNewConversation(recipientId: String): String
    suspend fun findExistingConversationWithUser(userId: String): String?
}