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
import com.google.firebase.firestore.CollectionReference
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

    override suspend fun getRequestorUids(): List<String> {
        try {
            // Query the Firestore "requests" collection for requestors of the current user
            val querySnapshot = firebaseFirestore
                .collection(Constants.REQUESTS)
                .whereEqualTo("requested", getCurrentUserID())
                .get()
                .await()

            // Extract UIDs of requestors from the query results
            val requestorUids = mutableListOf<String>()
            for (document in querySnapshot.documents) {
                val requestorUid = document.getString("requestor")
                if (requestorUid != null) {
                    requestorUids.add(requestorUid)
                }
            }

            return requestorUids
        } catch (e: Exception) {
            // Handle exceptions (e.g., Firestore network errors)
            e.printStackTrace()
        }
        // Return an empty list if the current user is not signed in or an error occurs
        return emptyList()
    }

    // Check if the user with the given ID is a friend of the current user
//    override suspend fun isUserAFriend(userId: String): Boolean {
//        val currentUserUid = getCurrentUserID() // Replace with your method to get the current user's ID
//
//        try {
//            val friendsSnapshot = firebaseFirestore.collection("friends")
//                .whereArrayContains("profiles", currentUserUid)
//                .get()
//                .await()
//
//            // Check if the given user ID is in the list of friends
//            return friendsSnapshot.documents.any { document ->
//                val profiles = document.get("profiles") as? List<String>
//                profiles?.contains(userId) == true
//            }
//        } catch (e: Exception) {
//            // Handle exceptions (e.g., Firestore network errors)
//            e.printStackTrace()
//            return false // Return false on error
//        }
//    }

    // Check if the user with the given ID sent a friend request to the current user
    override suspend fun didUserSendFriendRequest(userId: String): Boolean {
        val currentUserUid = getCurrentUserID() // Replace with your method to get the current user's ID

        try {
            val requestsSnapshot = firebaseFirestore.collection("requests")
                .whereEqualTo("requestor", userId)
                .whereEqualTo("requested", currentUserUid)
                .get()
                .await()

            // Check if there is a request from the given user to the current user
            return !requestsSnapshot.isEmpty
        } catch (e: Exception) {
            // Handle exceptions (e.g., Firestore network errors)
            e.printStackTrace()
            return false // Return false on error
        }
    }

    override suspend fun getNewPeopleIds(): List<String> {
        try {
            // Get the UID of the currently signed-in user
            val currentUserUid = getCurrentUserID()

            // Get a list of all users from Firestore
            val allUsers = getAllPeople()

            // Fetch friends and requests data for the current user
            val friendsQuery = firebaseFirestore.collection("friends")
                .whereEqualTo("profile1", currentUserUid)
                .get()
                .await()

            val requestsQuery = firebaseFirestore.collection("requests")
                .whereEqualTo("requestor", currentUserUid)
                .get()
                .await()

            val friendUids = friendsQuery.documents.mapNotNull { it.getString("profile2") }
            val requestUids = requestsQuery.documents.mapNotNull { it.getString("requested") }

            // Filter out user IDs of people who are neither friends nor have had any previous requests with the current user
            val filteredUserIds = allUsers.filter { user ->
                val userUid = user.uid

                val isFriend = userUid !in friendUids && currentUserUid !in friendUids
                val hasNoRequests = userUid !in requestUids && currentUserUid !in requestUids

                isFriend && hasNoRequests
            }.map { it.uid }

            return filteredUserIds
        } catch (e: Exception) {
            // Handle exceptions (e.g., Firestore network errors)
            e.printStackTrace()
            return emptyList()
        }
    }


    override suspend fun requestFriend(requestedUid: String) {
        try {
            // Add a request to the "requests" collection
            firebaseFirestore.collection("requests").add(mapOf("requestor" to getCurrentUserID(), "requested" to requestedUid))
                .await()
        } catch (e: Exception) {
            // Handle exceptions (e.g., Firestore network errors)
            e.printStackTrace()
        }
    }

    override suspend fun approveFriend(requestorUid: String) {
        try {
            // Add both users as friends in the "friends" collection
            firebaseFirestore.collection("friends").add(mapOf("profile1" to requestorUid, "profile2" to getCurrentUserID()))
                .await()

            // Remove the request from the "requests" collection
            val querySnapshot = firebaseFirestore
                .collection("requests")
                .whereEqualTo("requestor", requestorUid)
                .whereEqualTo("requested", getCurrentUserID())
                .get()
                .await()

            for (document in querySnapshot.documents) {
                document.reference.delete().await()
            }
        } catch (e: Exception) {
            // Handle exceptions (e.g., Firestore network errors)
            e.printStackTrace()
        }
    }

    override suspend fun removeFriend(friendUid: String) {
        // Assuming a user can remove a friend
        try {
            // Remove both friends from the "friends" collection
            firebaseFirestore.collection("friends")
                .whereEqualTo("profile1", getCurrentUserID())
                .whereEqualTo("profile2", friendUid)
                .get()
                .await()
                .documents
                .forEach { it.reference.delete().await() }

            firebaseFirestore.collection("friends")
                .whereEqualTo("profile1", friendUid)
                .whereEqualTo("profile2", getCurrentUserID())
                .get()
                .await()
                .documents
                .forEach { it.reference.delete().await() }
        } catch (e: Exception) {
            // Handle exceptions (e.g., Firestore network errors)
            e.printStackTrace()
        }
    }

    override suspend fun isFriend(profileUid: String): Boolean {
        try {
            // Check if there is a friendship document between the current user and the given profile in the "friends" collection
            val querySnapshot = firebaseFirestore
                .collection("friends")
                .whereEqualTo("profile1", getCurrentUserID())
                .whereEqualTo("profile2", profileUid)
                .get()
                .await()

            val querySnapshot2 = firebaseFirestore
                .collection("friends")
                .whereEqualTo("profile1", profileUid)
                .whereEqualTo("profile2", getCurrentUserID())
                .get()
                .await()

            return !querySnapshot.isEmpty || !querySnapshot2.isEmpty
        } catch (e: Exception) {
            // Handle exceptions (e.g., Firestore network errors)
            e.printStackTrace()
            return false
        }
    }

    override suspend fun getAllPosts(): List<Post> = withContext(Dispatchers.IO) {
        try {
            val postsCollection = firebaseFirestore.collection(Constants.POSTS).get().await()
            val postsList = postsCollection.documents.mapNotNull { document ->
                val post = document.toObject(Post::class.java)
                if (post != null && post.author == getCurrentUserID()) {
                    post.isMine = true
                }
                post
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

            return true
        } catch (e: Exception) {
            return false
        }
    }

    override suspend fun updatePost(postId: String, updatedTitle: String, updatedDesc: String, updatedTags: List<String>): Boolean {
        try {
            val postRef = firebaseFirestore.collection(Constants.POSTS).document(postId)
            postRef.update(
                mapOf(
                    "title" to updatedTitle,
                    "desc" to updatedDesc,
                    "tags" to updatedTags,
                    "published" to getCurrentDateTime()
                )
            ).await()
            return true
        } catch (e: Exception) {
            return false
        }
    }

    override suspend fun deletePost(postId: String): Boolean {
        try {
            val postRef = firebaseFirestore.collection(Constants.POSTS).document(postId)
            postRef.delete().await()
            return true
        } catch (e: Exception) {
            return false
        }
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


//    override suspend fun getAllPosts(): List<Post> = withContext(Dispatchers.IO) {
//        try {
//            val postsCollection = firebaseFirestore.collection(Constants.POSTS).get().await()
//            val postsList = postsCollection.documents.mapNotNull { document ->
//                document.toObject(Post::class.java)
//            }
//            postsList
//        } catch (e: Exception) {
//            // Handle exceptions here
//            emptyList()
//        }
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

//    override suspend fun getAllPosts(): List<Post> = withContext(Dispatchers.IO) {
//        try {
//            val postsCollection = firebaseFirestore.collection(Constants.POSTS).get().await()
//            val postsList = postsCollection.documents.mapNotNull { document ->
//                document.toObject(Post::class.java)?.apply {
//                    // Initialize default values
//                    isLiked = false
//                    likes = 0
//                }
//            }
//            ccLog.e("CoreRepositoryImpl/getAllPosts", "postsList.size = ${postsList.size}")
//            // Fetch likes for all posts
//            val likesQuery = firebaseFirestore.collectionGroup("likes")
//                .whereIn("post", postsList.map { it.uid })
//                .get()
//                .await()
//            ccLog.e("CoreRepositoryImpl/getAllPosts", "likesQuery = ${likesQuery}")
//
//            // Process like data and update posts
//            val postLikesMap = mutableMapOf<String, Int>()
//            val likedPostIds = mutableSetOf<String>()
//
//            for (likeDocument in likesQuery.documents) {
//                val postId = likeDocument.getString("post") ?: continue
//                postLikesMap[postId] = postLikesMap.getOrDefault(postId, 0) + 1
//
//                if (likeDocument.getString("user") == getCurrentUserID()) {
//                    likedPostIds.add(postId)
//                }
//            }
//            ccLog.e("CoreRepositoryImpl/getAllPosts", "postLikesMap.size = ${postLikesMap.size}")
//            ccLog.e("CoreRepositoryImpl/getAllPosts", "likedPostIds.size = ${likedPostIds.size}")
//
//            // Update post data with like count and liked status
//            for (post in postsList) {
//                post.likes = postLikesMap[post.uid] ?: 0
//                post.isLiked = post.uid in likedPostIds
//            }
//            ccLog.e("CoreRepositoryImpl/getAllPosts", "postsList.size = ${postsList.size}")
//            postsList
//        } catch (e: Exception) {
//            // Handle exceptions here
//            emptyList()
//        }
//    }

    override fun getMessagesCollection(): CollectionReference {
        return firebaseFirestore.collection(Constants.MESSAGE)
    }

    override suspend fun addComment(postUid: String, desc: String): Boolean {
        try {
            val newCommentRef = firebaseFirestore.collection(Constants.COMMENTS).document() // Generate a new document ID
            newCommentRef.set(Comment(
                uid = newCommentRef.id,
                postUid = postUid,
                author = getCurrentUserID(),
                desc = desc,
                published = getCurrentDateTime()
            )) // Add the post to Firebase with the generated ID

            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    override suspend fun removeComment(id: String): Boolean {
        try {
            // Delete the comment document with the specified UID
            firebaseFirestore.collection(Constants.COMMENTS).document(id)
                .delete()
                .await()
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    /* ----------------------------------------------------------------------------------------- *
     | Contacts Feature
     * ----------------------------------------------------------------------------------------- */
    /*
    Get a list of user's contacts
     */
    override suspend fun getContacts(): List<ProfileData> {
        // Fetch the list of contact UIDs from the current user's document
        val currentUserUid = getCurrentUserID() // Implement this function to get the current user's UID
        val userDocument = firebaseFirestore.collection(Constants.PROFILE_DATA).document(currentUserUid).get().await()
        val contactUids = userDocument.toObject(ProfileData::class.java)?.contacts ?: emptyList()

        // Fetch the profile data for each contact UID
        val contacts = mutableListOf<ProfileData>()
        for (contactUid in contactUids) {
            val contactDocument = firebaseFirestore.collection(Constants.PROFILE_DATA).document(contactUid).get().await()
            val contactProfile = contactDocument.toObject(ProfileData::class.java)
            contactProfile?.let { contacts.add(it) }
        }

        return contacts
    }

    /*
    Add a contact to the current user's list of contacts
     */
    override suspend fun addContact(contactUid: String): Boolean {
        val currentUserUid = getCurrentUserID() // Implement this function to get the current user's UID
        val userRef = firebaseFirestore.collection(Constants.PROFILE_DATA).document(currentUserUid)

        return try {
            // Add the new contact UID to the contacts list
            userRef.update("contacts", FieldValue.arrayUnion(contactUid)).await()
            true
        } catch (e: Exception) {
            // Handle any errors that occur
            false
        }
    }

    /*
    Remove a contact from the current user's list of contacts
     */
    override suspend fun removeContact(contactUid: String): Boolean {
        val currentUserUid = getCurrentUserID() // Implement this function to get the current user's UID
        val userRef = firebaseFirestore.collection(Constants.PROFILE_DATA).document(currentUserUid)

        return try {
            // Remove the contact UID from the contacts list
            userRef.update("contacts", FieldValue.arrayRemove(contactUid)).await()
            true
        } catch (e: Exception) {
            // Handle any errors that occur
            false
        }
    }



    override suspend fun markConversationAsRead(conversationId: String) {
        // Assuming you have a "conversations" collection in Firestore
        val conversationRef = firebaseFirestore.collection(Constants.CONVERSATION).document(conversationId)

        // Update the isUnread field to false
        conversationRef.update("isRead", true)
            .addOnSuccessListener {
                // Successfully marked as read
            }
            .addOnFailureListener { e ->
                // Handle the error
            }
    }

    override suspend fun markConversationUnread(conversationId: String) {
        // Assuming you have a "conversations" collection in Firestore
        val conversationRef = firebaseFirestore.collection(Constants.CONVERSATION).document(conversationId)

        // Update the isUnread field to true
        conversationRef.update("isRead", false)
            .addOnSuccessListener {
                // Successfully marked as unread
            }
            .addOnFailureListener { e ->
                // Handle the error
            }
    }





}