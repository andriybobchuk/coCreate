package com.andriybobchuk.cocreate.feature.profile.domain.model

data class ProfileData(
    val name: String = "Default name",
    val position: String = "",
    val city: String = "",
    val avatar: String = "",
    val email: String = "",
    val github: String = "",
    val website: String = "",
    val linkedin: String = "",
    val tools: List<String> = listOf(),
    val programmingLanguages: List<String> = listOf(),
    val languages: List<String> = listOf(),
)
