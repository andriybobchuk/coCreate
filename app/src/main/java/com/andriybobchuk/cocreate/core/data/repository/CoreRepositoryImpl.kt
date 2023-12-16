package com.andriybobchuk.cocreate.core.data.repository

import android.util.Log
import com.andriybobchuk.cocreate.core.Constants
import com.andriybobchuk.cocreate.core.domain.model.Comment
import com.andriybobchuk.cocreate.core.domain.model.Post
import com.andriybobchuk.cocreate.feature.messages.domain.model.Conversation
import com.andriybobchuk.cocreate.feature.messages.domain.model.Message
import com.andriybobchuk.cocreate.feature.profile.domain.model.ProfileData
import com.andriybobchuk.cocreate.util.ccLog
import com.andriybobchuk.cocreate.util.getCurrentDateTime
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CoreRepositoryImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
): CoreRepository {

    override fun getCurrentUserID(): String {
        // An Instance of currentUser using FirebaseAuth
        val currentUser = firebaseAuth.currentUser
        // A variable to assign the currentUserId if it is not null or else it will be blank.
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }
        return currentUserID
    }

    override suspend fun getAllPeople(): List<ProfileData> = withContext(Dispatchers.IO) {
        try {
            val peopleCollection = firebaseFirestore.collection(Constants.PROFILE_DATA).get().await()
            val peopleList = peopleCollection.documents.mapNotNull { document ->
                document.toObject(ProfileData::class.java)
            }
            peopleList
        } catch (e: Exception) {
            // Handle exceptions here
            emptyList()
        }
    }

    override suspend fun getProfileDataById(id: String): ProfileData {
        var profileData = ProfileData()
        try {
            profileData = firebaseFirestore
                .collection(Constants.PROFILE_DATA)
                .document(id)
                .get()
                .await()
                .toObject(ProfileData::class.java)!!

        } catch (e: FirebaseFirestoreException) {
            Log.d("error", e.toString())
        }
        return profileData
    }
}