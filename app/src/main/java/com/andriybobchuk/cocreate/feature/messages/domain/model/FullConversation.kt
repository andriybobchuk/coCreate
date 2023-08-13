package com.andriybobchuk.cocreate.feature.messages.domain.model

import com.andriybobchuk.cocreate.feature.profile.domain.model.ProfileData

data class FullConversation(
    val recipientData: ProfileData,
    val lastMessage: Message,
    val convoData: Conversation
)