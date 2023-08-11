package com.andriybobchuk.cocreate.core.domain.model

import com.andriybobchuk.cocreate.feature.profile.domain.model.ProfileData

data class AuthorComment(
    val body: Comment = Comment(),
    val authorData: ProfileData = ProfileData()
)