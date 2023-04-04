package com.andriybobchuk.cocreate.feature.auth.data.repository

import com.andriybobchuk.cocreate.util.Resource
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    fun loginUser(email: String, password: String): Flow<Resource<AuthResult>>
    fun registerUser(email: String, password: String): Flow<Resource<AuthResult>>

    fun googleLogin(credential: AuthCredential): Flow<Resource<AuthResult>>
    fun facebookLogin(credential: AuthCredential): Flow<Resource<AuthResult>>

}