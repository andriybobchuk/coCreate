package com.andriybobchuk.cocreate.feature.auth.presentation.use_case.login

import com.google.firebase.auth.AuthResult

data class GoogleLoginState (
    val isLoading: Boolean = false,
    val isSuccess : AuthResult? = null,
    val isError: String? = ""
)