package com.andriybobchuk.cocreate.core.data.repository

import com.andriybobchuk.cocreate.core.domain.model.Comment
import com.andriybobchuk.cocreate.core.domain.model.Post
import com.andriybobchuk.cocreate.feature.messages.domain.model.Conversation
import com.andriybobchuk.cocreate.feature.messages.domain.model.Message
import com.andriybobchuk.cocreate.feature.profile.domain.model.ProfileData

interface CoreRepository {

    // Read
    fun getCurrentUserID(): String
    suspend fun getCurrentUserFriends(): List<String>
    suspend fun getRequestorUids(): List<String>
    suspend fun getNewPeopleIds(): List<String>
    suspend fun requestFriend(requestedUid: String)
    suspend fun approveFriend(requestorUid: String)
    suspend fun removeFriend(friendUid: String)
    suspend fun isFriend(profileUid: String): Boolean
    suspend fun getAllPosts(): List<Post>
    suspend fun getMyPosts(): List<Post>
    suspend fun getAllPeople(): List<ProfileData>
    suspend fun getCommentsByPostId(id: String): List<Comment>
    suspend fun getPersonByID(id: String): ProfileData
    suspend fun getProfileDataById(id: String): ProfileData
    suspend fun getPostDataById(id: String): Post
    suspend fun getConversationsByUserId(userId: String): List<Conversation>
    suspend fun getMessageById(id: String): Message
    suspend fun getConversationById(chatId: String): Conversation
    suspend fun getMessagesForChat(chatId: String): List<Message>

    //suspend fun isUserAFriend(userId: String): Boolean
    suspend fun didUserSendFriendRequest(userId: String): Boolean

    // Create
    suspend fun addNewPost(title: String, desc: String, tags: List<String>): Boolean
    suspend fun updatePost(postId: String, updatedTitle: String, updatedDesc: String, updatedTags: List<String>): Boolean
    suspend fun deletePost(postId: String): Boolean

    suspend fun addMessageToConversation(
        conversationId: String,
        content: String,
        senderId: String,
        time: String
    )

    suspend fun likePost(postId: String)
    suspend fun unlikePost(postId: String)

    suspend fun getLikeCountForPost(postId: String): Int
    suspend fun isPostLikedByCurrentUser(postId: String): Boolean
}