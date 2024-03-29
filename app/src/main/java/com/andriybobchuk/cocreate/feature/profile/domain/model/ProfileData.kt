package com.andriybobchuk.cocreate.feature.profile.domain.model

data class ProfileData(
    val uid: String = "",
    val name: String = "",
    val position: String = "",
    val city: String = "",
    val desc: String = "",
    val avatar: String = "",
    val email: String = "",
    val github: String = "",
    val website: String = "",
    val tags: List<String> = listOf(),
    val contacts: List<String> = listOf()
)
