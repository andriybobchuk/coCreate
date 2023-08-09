package com.andriybobchuk.cocreate.core.data.repository

import com.andriybobchuk.cocreate.feature.profile.domain.model.ProfileData

interface CoreRepository {

    fun getCurrentUserID(): String
    suspend fun getCurrentUserFriends(): List<String>
    suspend fun getPersonByID(id: String): ProfileData
    suspend fun getProfileDataById(id: String): ProfileData
}