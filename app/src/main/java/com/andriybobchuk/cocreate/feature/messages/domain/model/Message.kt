package com.andriybobchuk.cocreate.feature.messages.domain.model

data class Message(
    val id: String,
    val text: String,
    val senderId: String,
    val time: String, // You can use the formatted time like "Sep 21, '23 at 8:34"
    val isMyMessage: Boolean // Indicates whether the message is sent by the current user
)