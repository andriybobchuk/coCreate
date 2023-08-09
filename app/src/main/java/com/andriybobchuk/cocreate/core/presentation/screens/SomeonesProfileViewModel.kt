package com.andriybobchuk.cocreate.core.presentation.screens

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andriybobchuk.cocreate.core.data.repository.CoreRepository
import com.andriybobchuk.cocreate.feature.profile.data.repository.ProfileRepository
import com.andriybobchuk.cocreate.feature.profile.domain.model.ProfileData
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SomeonesProfileViewModel @Inject constructor(
    private val repository: CoreRepository,
    private val auth: FirebaseAuth
) : ViewModel() {
    val state = mutableStateOf(ProfileData())

    init {
        //getProfileDataById("")
    }

    private fun getProfileDataById(id: String) {
        viewModelScope.launch {
            state.value = repository.getProfileDataById(id)
        }
    }
}