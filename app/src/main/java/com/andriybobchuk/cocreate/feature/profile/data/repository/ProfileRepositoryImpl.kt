package com.andriybobchuk.cocreate.feature.profile.data.repository

import android.util.Log
import com.andriybobchuk.cocreate.core.Constants
import com.andriybobchuk.cocreate.core.data.repository.CoreRepository
import com.andriybobchuk.cocreate.feature.profile.domain.model.ProfileData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val coreRepository: CoreRepository
): ProfileRepository {
    override suspend fun getProfileData(): ProfileData {

        var profileData = ProfileData()

        try {
            profileData = firebaseFirestore
                .collection(Constants.PROFILE_DATA)
                .document(coreRepository.getCurrentUserID())
                .get()
                .await()
                .toObject(ProfileData::class.java)!!

        } catch (e: FirebaseFirestoreException) {
            Log.d("error", e.toString())
        }

        return profileData
    }
}