package com.andriybobchuk.cocreate.feature.auth.data.repository

import com.andriybobchuk.cocreate.core.Constants
import com.andriybobchuk.cocreate.core.data.repository.CoreRepository
import com.andriybobchuk.cocreate.feature.profile.domain.model.ProfileData
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ContactsRepositoryImpl @Inject constructor(
    private val coreRepository: CoreRepository,
    private val firebaseFirestore: FirebaseFirestore
) : ContactsRepository {
    override fun getCurrentUserID(): String {
        return coreRepository.getCurrentUserID()
    }

    override suspend fun getAllPeople(): List<ProfileData> {
        return coreRepository.getAllPeople()
    }

    override suspend fun getProfileDataById(id: String): ProfileData {
        return coreRepository.getProfileDataById(id)
    }

    override suspend fun getContacts(): List<ProfileData> {
        // Fetch the list of contact UIDs from the current user's document
        val currentUserUid = getCurrentUserID() // Implement this function to get the current user's UID
        val userDocument = firebaseFirestore.collection(Constants.PROFILE_DATA).document(currentUserUid).get().await()
        val contactUids = userDocument.toObject(ProfileData::class.java)?.contacts ?: emptyList()

        // Fetch the profile data for each contact UID
        val contacts = mutableListOf<ProfileData>()
        for (contactUid in contactUids) {
            val contactDocument = firebaseFirestore.collection(Constants.PROFILE_DATA).document(contactUid).get().await()
            val contactProfile = contactDocument.toObject(ProfileData::class.java)
            contactProfile?.let { contacts.add(it) }
        }

        return contacts
    }

    override suspend fun addContact(contactUid: String): Boolean {
        val currentUserUid = getCurrentUserID() // Implement this function to get the current user's UID
        val userRef = firebaseFirestore.collection(Constants.PROFILE_DATA).document(currentUserUid)
        return try {
            // Add the new contact UID to the contacts list
            userRef.update("contacts", FieldValue.arrayUnion(contactUid)).await()
            true
        } catch (e: Exception) {
            // Handle any errors that occur
            false
        }
    }

    override suspend fun removeContact(contactUid: String): Boolean {
        val currentUserUid = getCurrentUserID() // Implement this function to get the current user's UID
        val userRef = firebaseFirestore.collection(Constants.PROFILE_DATA).document(currentUserUid)
        return try {
            // Remove the contact UID from the contacts list
            userRef.update("contacts", FieldValue.arrayRemove(contactUid)).await()
            true
        } catch (e: Exception) {
            // Handle any errors that occur
            false
        }
    }
}