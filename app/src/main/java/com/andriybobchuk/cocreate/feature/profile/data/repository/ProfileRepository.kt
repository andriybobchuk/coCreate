package com.andriybobchuk.cocreate.feature.profile.data.repository

import com.andriybobchuk.cocreate.feature.profile.domain.model.ProfileData

interface ProfileRepository {

    suspend fun getProfileData(): ProfileData

}