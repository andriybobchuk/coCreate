package com.andriybobchuk.cocreate.feature.auth.presentation.use_case.register

data class RegisterState(
    val isLoading: Boolean = false,
    val isSuccess : String? = "",
    val isError: String? = ""
)

