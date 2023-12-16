package com.andriybobchuk.cocreate.feature.auth.data.repository

import android.util.Log
import com.andriybobchuk.cocreate.core.Constants
import com.andriybobchuk.cocreate.core.data.repository.CoreRepository
import com.andriybobchuk.cocreate.core.domain.model.Comment
import com.andriybobchuk.cocreate.core.domain.model.Post
import com.andriybobchuk.cocreate.feature.profile.domain.model.ProfileData
import com.andriybobchuk.cocreate.util.getCurrentDateTime
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val coreRepository: CoreRepository,
    private val firebaseFirestore: FirebaseFirestore
) : PostRepository {
    override fun getCurrentUserID(): String {
        return coreRepository.getCurrentUserID()
    }

    override suspend fun getAllPeople(): List<ProfileData> {
        return coreRepository.getAllPeople()
    }

    override suspend fun getProfileDataById(id: String): ProfileData {
        return coreRepository.getProfileDataById(id)
    }

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
}