package com.andriybobchuk.cocreate.core.data.repository

import com.andriybobchuk.cocreate.feature.profile.domain.model.ProfileData

interface CoreRepository {

    fun getCurrentUserID(): String
    suspend fun getCurrentUserFriends(): List<String>
    suspend fun getPersonByID(Id: String): ProfileData

}