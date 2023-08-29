package com.andriybobchuk.cocreate.feature.messages.domain.model

import com.andriybobchuk.cocreate.feature.profile.domain.model.ProfileData

data class FullPrivateChat(
    val chatData: Conversation = Conversation(), // Aggregator of IDs: party 1 ID, party 2 ID, last message ID, UID
    val recipientData: ProfileData = ProfileData(), // Recipient avatar, name
    val messages: List<Message> = listOf(),
)