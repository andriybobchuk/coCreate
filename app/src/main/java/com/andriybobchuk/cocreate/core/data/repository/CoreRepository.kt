package com.andriybobchuk.cocreate.core.data.repository

import com.andriybobchuk.cocreate.core.domain.model.Post
import com.andriybobchuk.cocreate.feature.profile.domain.model.ProfileData

interface CoreRepository {

    fun getCurrentUserID(): String
    suspend fun getCurrentUserFriends(): List<String>
    suspend fun getAllPosts(): List<Post>
    suspend fun getPersonByID(id: String): ProfileData
    suspend fun getProfileDataById(id: String): ProfileData
}