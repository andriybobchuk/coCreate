package com.andriybobchuk.cocreate.core.data.repository

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class CoreRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
): CoreRepository {

    /**
     * A function for getting the user id of current logged user.
     */
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
}