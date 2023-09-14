package com.andriybobchuk.cocreate.core.domain.model

data class Post(
    var uid: String = "",
    val author: String = "",
    val title: String = "",
    val desc: String = "",
    val published: String = "",
    val tags: List<String> = listOf(),
    var isLiked: Boolean = false,
    var likes: Int = 0,
    val comments: Int = 0,
)