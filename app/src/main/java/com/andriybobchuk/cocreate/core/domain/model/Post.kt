package com.andriybobchuk.cocreate.core.domain.model

data class Post(
    val authorId: String = "",
    val title: String = "",
    val desc: String = "",
    val published: String = "",
    val tags: List<String> = listOf(),
)