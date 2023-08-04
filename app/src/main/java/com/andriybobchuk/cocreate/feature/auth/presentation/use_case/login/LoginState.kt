package com.andriybobchuk.cocreate.feature.auth.presentation.use_case.login

data class LoginState(
    val isLoading: Boolean = false,
    val isSuccess : String? = "",
    val isError: String? = ""
)
