package com.andriybobchuk.cocreate.feature.messages.domain.model

import com.andriybobchuk.cocreate.feature.profile.domain.model.ProfileData

data class FullConversation(
    val recipientData: ProfileData, // Recipient avatar, name
    val lastMessage: Message, // Last message: content, time
    val convoData: Conversation // Aggregator of IDs: party 1 ID, party 2 ID, last message ID, UID
)