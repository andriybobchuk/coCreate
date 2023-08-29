package com.andriybobchuk.cocreate.feature.messages.domain.model

data class Message(
    val convoId: String = "",
    val id: String = "",
    val content: String = "",
    val senderId: String = "",
    val time: String = "", // You can use the formatted time like "Sep 21, '23 at 8:34"
    var isMyMessage: Boolean = false // Indicates whether the message is sent by the current user
)