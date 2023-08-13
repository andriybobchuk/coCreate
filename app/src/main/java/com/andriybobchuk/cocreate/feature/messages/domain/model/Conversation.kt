package com.andriybobchuk.cocreate.feature.messages.domain.model

data class Conversation(
    val uid: String,
    val senderId: String,
    val recipientId: String,
    val lastMessageId: String,
)