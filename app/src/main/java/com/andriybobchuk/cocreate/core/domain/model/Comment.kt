package com.andriybobchuk.cocreate.core.domain.model

data class Comment(
    val uid: String = "",
    val postUid: String = "",
    val author: String = "",
    val desc: String = "",
    val published: String = "",
)