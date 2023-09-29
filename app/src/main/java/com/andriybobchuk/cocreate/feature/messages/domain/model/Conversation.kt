package com.andriybobchuk.cocreate.feature.messages.domain.model

data class Conversation(
    val uid: String = "",
    val participants: List<String> = listOf(),
    val lastMessageId: String = "",
    val isRead: Boolean = false,
)