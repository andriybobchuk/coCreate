package com.andriybobchuk.cocreate.core.data.repository

import com.andriybobchuk.cocreate.core.domain.model.Comment
import com.andriybobchuk.cocreate.core.domain.model.Post
import com.andriybobchuk.cocreate.feature.messages.domain.model.Conversation
import com.andriybobchuk.cocreate.feature.messages.domain.model.Message
import com.andriybobchuk.cocreate.feature.profile.domain.model.ProfileData
import com.google.firebase.firestore.CollectionReference

interface CoreRepository {
    fun getCurrentUserID(): String
    suspend fun getAllPeople(): List<ProfileData>
    suspend fun getProfileDataById(id: String): ProfileData
}