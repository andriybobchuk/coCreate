package com.andriybobchuk.cocreate.feature.auth.data.repository

import com.andriybobchuk.cocreate.feature.profile.domain.model.ProfileData

interface ContactsRepository {

    // Core
    fun getCurrentUserID(): String
    suspend fun getAllPeople(): List<ProfileData>
    suspend fun getProfileDataById(id: String): ProfileData

    // Contacts
    suspend fun getContacts(): List<ProfileData>
    suspend fun addContact(contactUid: String): Boolean
    suspend fun removeContact(contactUid: String): Boolean
}