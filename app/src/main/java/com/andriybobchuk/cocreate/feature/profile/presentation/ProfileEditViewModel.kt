package com.andriybobchuk.cocreate.feature.profile.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.andriybobchuk.cocreate.core.data.repository.CoreRepository
import com.andriybobchuk.cocreate.feature.profile.data.repository.ProfileRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileEditViewModel @Inject constructor(
    private val repository: ProfileRepository,
    private val coreRepository: CoreRepository,
    private val auth: FirebaseAuth
) : ViewModel() {
    // Define LiveData for each editable field
    private val _name = MutableLiveData<String>()
    val name: LiveData<String> get() = _name

    private val _position = MutableLiveData<String>()
    val position: LiveData<String> get() = _position

    private val _city = MutableLiveData<String>()
    val city: LiveData<String> get() = _city

    // Define a LiveData to track whether the profile has been updated
    private val _profileUpdated = MutableLiveData<Boolean>()
    val profileUpdated: LiveData<Boolean> get() = _profileUpdated

    init {
        // Initialize LiveData with the current user's profile data
        _name.value = "John Doe" // Replace with actual data
        _position.value = "Software Developer" // Replace with actual data
        _city.value = "New York" // Replace with actual data
    }

    // Function to update the profile
    fun updateProfile(name: String, position: String, city: String) {
        // Perform the update operation here (e.g., call a repository function)
        // Update the LiveData values if the operation is successful
        _name.value = name
        _position.value = position
        _city.value = city

        // Set the profileUpdated LiveData to true
        _profileUpdated.value = true
    }
}