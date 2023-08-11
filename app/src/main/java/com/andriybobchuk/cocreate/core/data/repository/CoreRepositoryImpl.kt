package com.andriybobchuk.cocreate.core.data.repository

import android.util.Log
import com.andriybobchuk.cocreate.core.Constants
import com.andriybobchuk.cocreate.core.domain.model.AuthorPost
import com.andriybobchuk.cocreate.core.domain.model.Comment
import com.andriybobchuk.cocreate.core.domain.model.Person
import com.andriybobchuk.cocreate.core.domain.model.Post
import com.andriybobchuk.cocreate.feature.profile.domain.model.ProfileData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
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
}