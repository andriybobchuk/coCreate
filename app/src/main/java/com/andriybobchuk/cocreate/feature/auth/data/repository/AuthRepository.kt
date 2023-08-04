package com.andriybobchuk.cocreate.feature.auth.data.repository

import com.andriybobchuk.cocreate.feature.profile.domain.model.ProfileData
import com.andriybobchuk.cocreate.util.Resource
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    fun loginUser(email: String, password: String): Flow<Resource<AuthResult>>
    fun registerUser(name: String, email: String, password: String): Flow<Resource<AuthResult>>
    fun createBasicProfileInFirestore(profileData: ProfileData)

    fun googleLogin(credential: AuthCredential): Flow<Resource<AuthResult>>
    fun facebookLogin(credential: AuthCredential): Flow<Resource<AuthResult>>

}