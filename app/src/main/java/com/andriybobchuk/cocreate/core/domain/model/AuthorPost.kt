package com.andriybobchuk.cocreate.core.domain.model

import com.andriybobchuk.cocreate.feature.profile.domain.model.ProfileData

data class AuthorPost(
    val postBody: Post = Post(),
    val postAuthor: ProfileData = ProfileData()
)