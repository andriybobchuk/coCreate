package com.andriybobchuk.cocreate.feature.auth.data.repository

import com.andriybobchuk.cocreate.core.Constants
import com.andriybobchuk.cocreate.core.data.repository.CoreRepository
import com.andriybobchuk.cocreate.feature.messages.domain.model.Conversation
import com.andriybobchuk.cocreate.feature.messages.domain.model.Message
import com.andriybobchuk.cocreate.feature.profile.domain.model.ProfileData
import com.andriybobchuk.cocreate.util.ccLog
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MessengerRepositoryImpl @Inject constructor(
    private val coreRepository: CoreRepository,
    private val firebaseFirestore: FirebaseFirestore
) : MessengerRepository {
    override fun getCurrentUserID(): String {
        return coreRepository.getCurrentUserID()
    }

    override suspend fun getAllPeople(): List<ProfileData> {
        return coreRepository.getAllPeople()
    }

    override suspend fun getProfileDataById(id: String): ProfileData {
        return coreRepository.getProfileDataById(id)
    }

    override fun getMessagesCollection(): CollectionReference {
        return firebaseFirestore.collection(Constants.MESSAGE)
    }

    override suspend fun getConversationsByUserId(userId: String): List<Conversation> {
        return try {
            val conversationsCollection = firebaseFirestore.collection(Constants.CONVERSATION)
                .whereArrayContains("participants", userId)
                .get()
                .await()

            ccLog.d("CoreRepositoryImpl", "conversationsCollection.documents.size = " + conversationsCollection.documents.size)
            val conversations = conversationsCollection.documents.mapNotNull { document ->
                document.toObject(Conversation::class.java)
            }
            ccLog.d("CoreRepositoryImpl", "conversations.size = " + conversations.size)
            return conversations
        } catch (e: Exception) {
            // Handle exceptions here
            ccLog.e("CoreRepositoryImpl", e.message)
            emptyList()
        }
    }

    override suspend fun getMessageById(id: String): Message {
        var message = Message("", "", "", "", "", false)
        try {
            message = firebaseFirestore
                .collection(Constants.MESSAGE)
                .document(id)
                .get()
                .await()
                .toObject(Message::class.java)!!

        } catch (e: FirebaseFirestoreException) {
            ccLog.e("CoreRepositoryImpl", e.message)
        }
        return message
    }

    override suspend fun getConversationById(chatId: String): Conversation {
        var chat = Conversation("", listOf(), "")
        ccLog.d("CoreRepositoryImpl", "chatId = $chatId")
        try {
            chat = firebaseFirestore
                .collection(Constants.CONVERSATION)
                .document(chatId)
                .get()
                .await()
                .toObject(Conversation::class.java)!!

        } catch (e: FirebaseFirestoreException) {
            ccLog.e("CoreRepositoryImpl", e.message)
        }
        ccLog.d("CoreRepositoryImpl", "chat = $chat")
        return chat
    }

    override suspend fun addMessageToConversation(
        conversationId: String,
        content: String,
        senderId: String,
        time: String
    ) {
        try {
            val message = hashMapOf(
                "content" to content,
                "convoId" to conversationId,
                "senderId" to senderId,
                "time" to time
            )
            // Add the message document to the "messages" collection
            firebaseFirestore
                .collection(Constants.MESSAGE)
                .add(message)
                .addOnSuccessListener { documentReference ->
                    // After adding the message, update the conversation's lastMessageId
                    val messageDocumentId = documentReference.id
                    firebaseFirestore
                        .collection(Constants.CONVERSATION)
                        .document(conversationId)
                        .update("lastMessageId", messageDocumentId)
                }
                .addOnFailureListener { e ->
                    // Handle the failure to add the message
                    ccLog.e("CoreRepositoryImpl", e.message)
                }
        } catch (e: Exception) {
            // Handle exceptions here
            ccLog.e("CoreRepositoryImpl", e.message)
        }
    }

    override suspend fun createNewConversation(recipientId: String): String {
        try {
            val participants = listOf(recipientId, getCurrentUserID())
            val conversation = hashMapOf(
                "participants" to participants,
                "lastMessageId" to "", // Initially, there's no last message
                "isRead" to false // Set to false for a new, unread conversation
            )
            // Add the conversation document to Firestore
            val documentReference = firebaseFirestore
                .collection(Constants.CONVERSATION)
                .add(conversation)
                .await() // Wait for the result
            // Extract the generated conversationId from the document reference
            val conversationId = documentReference.id
            // Update the conversation document with its UID
            firebaseFirestore
                .collection(Constants.CONVERSATION)
                .document(conversationId)
                .update("uid", conversationId)
                .await()
            return conversationId
        } catch (e: Exception) {
            // Handle exceptions here
            // You can show an error message to the user
            return "" // Return an empty string to indicate failure
        }
    }

    override suspend fun findExistingConversationWithUser(userId: String): String? {
        val currentUserUid = getCurrentUserID()
        // Query conversations where the first participant is the current user
        val query = firebaseFirestore
            .collection(Constants.CONVERSATION)
            .whereArrayContains("participants", currentUserUid)
        val result = query.get().await()
        for (document in result.documents) {
            val participants = document.get("participants") as? List<String>
            val lastMessageId = document.getString("lastMessageId") ?: ""
            // Check if the conversation contains the other user
            if (participants != null && participants.contains(userId) && lastMessageId.isNotEmpty()) {
                return document.id
            }
        }
        return null // No existing conversation found
    }
}