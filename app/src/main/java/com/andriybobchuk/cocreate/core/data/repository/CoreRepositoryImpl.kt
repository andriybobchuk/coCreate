package com.andriybobchuk.cocreate.core.data.repository

import android.util.Log
import com.andriybobchuk.cocreate.core.Constants
import com.andriybobchuk.cocreate.core.domain.model.Comment
import com.andriybobchuk.cocreate.core.domain.model.Post
import com.andriybobchuk.cocreate.feature.messages.domain.model.Conversation
import com.andriybobchuk.cocreate.feature.messages.domain.model.Message
import com.andriybobchuk.cocreate.feature.profile.domain.model.ProfileData
import com.andriybobchuk.cocreate.util.ccLog
import com.andriybobchuk.cocreate.util.getCurrentDateTime
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CoreRepositoryImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
): CoreRepository {

    /**
     * A function for getting the user id of current logged user.
     */
    override fun getCurrentUserID(): String {
        // An Instance of currentUser using FirebaseAuth
        val currentUser = firebaseAuth.currentUser

        // A variable to assign the currentUserId if it is not null or else it will be blank.
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }

        return currentUserID
    }

    override suspend fun getCurrentUserFriends(): List<String> {
        val currentUserUid = getCurrentUserID()
        if (currentUserUid != null) {
            val friendsCollection = firebaseFirestore.collection(Constants.FRIENDS)
            val friendsQuery = friendsCollection
                .whereEqualTo("profile1", currentUserUid)
                .get()
                .await()

            val friendsQuery2 = friendsCollection
                .whereEqualTo("profile2", currentUserUid)
                .get()
                .await()

            val friendUidsSet = HashSet<String>() // Using HashSet to store unique UIDs

            for (document in friendsQuery.documents) {
                friendUidsSet.add(document.getString("profile2") ?: "")
            }

            for (document in friendsQuery2.documents) {
                friendUidsSet.add(document.getString("profile1") ?: "")
            }

            return friendUidsSet.toList() // Convert the set back to a list
        }
        return emptyList()
    }

    override suspend fun getAllPosts(): List<Post> = withContext(Dispatchers.IO) {
        try {
            val postsCollection = firebaseFirestore.collection(Constants.POSTS).get().await()
            val postsList = postsCollection.documents.mapNotNull { document ->
                document.toObject(Post::class.java)
            }
            postsList
        } catch (e: Exception) {
            // Handle exceptions here
            emptyList()
        }
    }

    override suspend fun getMyPosts(): List<Post> = withContext(Dispatchers.IO) {
        try {
            val postsCollection = firebaseFirestore.collection(Constants.POSTS)
                .whereEqualTo("author", getCurrentUserID())
                .get()
                .await()

            val postsList = postsCollection.documents.mapNotNull { document ->
                document.toObject(Post::class.java)
            }
            postsList
        } catch (e: Exception) {
            // Handle exceptions here
            emptyList()
        }
    }

    override suspend fun getAllPeople(): List<ProfileData> = withContext(Dispatchers.IO) {
        try {
            val peopleCollection = firebaseFirestore.collection(Constants.PROFILE_DATA).get().await()
            val peopleList = peopleCollection.documents.mapNotNull { document ->
                document.toObject(ProfileData::class.java)
            }
            peopleList
        } catch (e: Exception) {
            // Handle exceptions here
            emptyList()
        }
    }

    override suspend fun getCommentsByPostId(id: String): List<Comment> {
        try {
            val commentsCollection = firebaseFirestore.collection(Constants.COMMENTS)
                .whereEqualTo("postUid", id)
                .get()
                .await()
            val commentList = commentsCollection.documents.mapNotNull { document ->
                document.toObject(Comment::class.java)
            }
            return commentList
        } catch (e: Exception) {
            return emptyList()
        }
    }

    // To be removed
    override suspend fun getPersonByID(id: String): ProfileData {
        var profileData = ProfileData()

        try {
            profileData = firebaseFirestore
                .collection(Constants.PROFILE_DATA)
                .document(id)
                .get()
                .await()
                .toObject(ProfileData::class.java)!!

        } catch (e: FirebaseFirestoreException) {
            Log.d("error", e.toString())
        }

        return profileData
    }

    override suspend fun getProfileDataById(id: String): ProfileData {

        var profileData = ProfileData()

        try {
            profileData = firebaseFirestore
                .collection(Constants.PROFILE_DATA)
                .document(id)
                .get()
                .await()
                .toObject(ProfileData::class.java)!!

        } catch (e: FirebaseFirestoreException) {
            Log.d("error", e.toString())
        }

        return profileData
    }

    override suspend fun getPostDataById(id: String): Post {

        var postData = Post()

        try {
            postData = firebaseFirestore
                .collection(Constants.POSTS)
                .document(id)
                .get()
                .await()
                .toObject(Post::class.java)!!

        } catch (e: FirebaseFirestoreException) {
            Log.d("error", e.toString())
        }

        return postData
    }

    // Create
    override suspend fun addNewPost(title: String, desc: String, tags: List<String>): Boolean {
        try {
            val newPostRef = firebaseFirestore.collection(Constants.POSTS).document() // Generate a new document ID

            newPostRef.set(Post(
                uid = newPostRef.id,
                author = getCurrentUserID(),
                title = title,
                desc = desc,
                published = getCurrentDateTime(),
                tags = tags,
                likes = 0,
                comments = 0,
            )) // Add the post to Firebase with the generated ID

//            firebaseFirestore
//                .collection(Constants.POSTS)
//                .add(
//                    ).await()
            return true
        } catch (e: Exception) {
            return false
        }
    }

    override suspend fun getConversationsByUserId(userId: String): List<Conversation> {
        return try {
            ccLog.d("CoreRepositoryImpl", "userId = $userId")

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

    override suspend fun getMessagesForChat(chatId: String): List<Message> {
        val messagesCollection = firebaseFirestore
            .collection(Constants.MESSAGE)
            .whereEqualTo("convoId", chatId)
            .get()
            .await()
        ccLog.d("CoreRepositoryImpl", "messagesCollection.size() = ${messagesCollection.size()}")

        val messages = mutableListOf<Message>()

        for (messageDocument in messagesCollection.documents) {
            val message = messageDocument.toObject(Message::class.java)
            if (message != null) {
                message.isMyMessage = (message.senderId == getCurrentUserID())
                messages.add(message)
            } else {
                ccLog.e("CoreRepositoryImpl", "message == null")
            }
        }

        ccLog.d("CoreRepositoryImpl", "messages.size = ${messages.size}")
        return messages
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

    override suspend fun likePost(postId: String) {
        val likeData = mapOf(
            "post" to postId,
            "user" to getCurrentUserID()
        )

        try {
            // Add a like entry to the Firestore Likes collection
            firebaseFirestore.collection("likes").add(likeData).await()
        } catch (e: Exception) {
            // Handle the error if liking the post fails

        }
    }

    override suspend fun unlikePost(postId: String) {
        try {
            // Find and delete the like document for this post by the current user
            val querySnapshot = firebaseFirestore.collection("likes")
                .whereEqualTo("post", postId)
                .whereEqualTo("user", getCurrentUserID())
                .get().await()

            if (!querySnapshot.isEmpty) {
                val likeDocument = querySnapshot.documents[0]
                likeDocument.reference.delete().await()
            }

        } catch (e: Exception) {
            // Handle the error if unliking the post fails

        }
    }

//    // Update like count for a post
//    fun updateLikeCount(postId: String, change: Int) {
//        val postsCollection = firebaseFirestore.collection("posts")
//        postsCollection.document(postId)
//            .update("likesCount", FieldValue.increment(change.toDouble()))
//            .addOnFailureListener { e ->
//                // Handle the error
//                Log.e("CoreRepositoryImpl",  "Error updating like count: $e")
//            }
//    }

    override suspend fun getLikeCountForPost(postId: String): Int {
        return try {
            val snapshot = firebaseFirestore
                .collection("likes")
                .whereEqualTo("post", postId)
                .get()
                .await()

            // Return the count of documents (likes) for the specified post
            snapshot.size()
        } catch (e: Exception) {
            // Handle exceptions (e.g., Firestore network errors)
            e.printStackTrace()
            -1 // Return -1 as the default like count on error
        }
    }

    override suspend fun isPostLikedByCurrentUser(postId: String): Boolean {
        return try {
            val snapshot = firebaseFirestore
                .collection("likes")
                .whereEqualTo("post", postId)
                .whereEqualTo("user", getCurrentUserID())
                .get()
                .await()

            // Return true if a like document for the current user and post exists
            snapshot.size() != 0
        } catch (e: Exception) {
            // Handle exceptions (e.g., Firestore network errors)
            e.printStackTrace()
            false // Return false as the default liked status on error
        }
    }






}