package com.andriybobchuk.cocreate.core.data.repository

import com.andriybobchuk.cocreate.core.domain.model.AuthorPost
import com.andriybobchuk.cocreate.core.domain.model.Comment
import com.andriybobchuk.cocreate.core.domain.model.Post
import com.andriybobchuk.cocreate.feature.profile.domain.model.ProfileData

interface CoreRepository {

    // Read
    fun getCurrentUserID(): String
    suspend fun getCurrentUserFriends(): List<String>
    suspend fun getAllPosts(): List<Post>
    suspend fun getMyPosts(): List<Post>
    suspend fun getAllPeople(): List<ProfileData>
    suspend fun getCommentsByPostId(id: String): List<Comment>
    suspend fun getPersonByID(id: String): ProfileData
    suspend fun getProfileDataById(id: String): ProfileData
    suspend fun getPostDataById(id: String): Post

    // Create
    suspend fun addNewPost(title: String, desc: String, tags: List<String>): Boolean
}