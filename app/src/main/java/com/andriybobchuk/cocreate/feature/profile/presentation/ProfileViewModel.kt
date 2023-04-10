package com.andriybobchuk.cocreate.feature.profile.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andriybobchuk.cocreate.feature.profile.data.repository.ProfileRepository
import com.andriybobchuk.cocreate.feature.profile.domain.model.ProfileData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository
) : ViewModel() {
    val state = mutableStateOf(ProfileData())

    init {
        getProfileData()
    }

    private fun getProfileData() {
        viewModelScope.launch {
            state.value = repository.getProfileData()
        }
    }
}