package com.andriybobchuk.cocreate.core.data.repository

import com.andriybobchuk.cocreate.core.domain.model.Person
import com.google.firebase.auth.FirebaseAuth

interface CoreRepository {

    fun getCurrentUserID(): String
    suspend fun getCurrentUserFriends(): List<String>
    suspend fun getPersonByID(Id: String): Person

}