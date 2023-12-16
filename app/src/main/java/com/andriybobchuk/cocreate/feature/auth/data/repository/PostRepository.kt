package com.andriybobchuk.cocreate.feature.auth.data.repository

import com.andriybobchuk.cocreate.core.domain.model.Comment
import com.andriybobchuk.cocreate.core.domain.model.Post
import com.andriybobchuk.cocreate.feature.profile.domain.model.ProfileData

interface PostRepository {

    // Core
    fun getCurrentUserID(): String
    suspend fun getAllPeople(): List<ProfileData>
    suspend fun getProfileDataById(id: String): ProfileData

    // Post
    suspend fun addNewPost(title: String, desc: String, tags: List<String>): Boolean
    suspend fun updatePost(postId: String, updatedTitle: String, updatedDesc: String, updatedTags: List<String>): Boolean
    suspend fun deletePost(postId: String): Boolean
    suspend fun getAllPosts(): List<Post>
    suspend fun getMyPosts(): List<Post>
    suspend fun getCommentsByPostId(id: String): List<Comment>
    suspend fun getPostDataById(id: String): Post
    suspend fun addComment(postUid: String, desc: String): Boolean
    suspend fun removeComment(id: String): Boolean
}