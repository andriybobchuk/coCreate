package com.andriybobchuk.cocreate.feature.profile.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.andriybobchuk.cocreate.core.data.repository.CoreRepository
import com.andriybobchuk.cocreate.core.domain.model.AuthorPost
import com.andriybobchuk.cocreate.feature.auth.data.repository.MessengerRepository
import com.andriybobchuk.cocreate.feature.auth.data.repository.PostRepository
import com.andriybobchuk.cocreate.feature.profile.data.repository.ProfileRepository
import com.andriybobchuk.cocreate.feature.profile.domain.model.ProfileData
import com.andriybobchuk.navigation.Screens
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository,
    private val coreRepository: CoreRepository,
    private val postRepository: PostRepository,
    private val auth: FirebaseAuth
) : ViewModel() {
    val state = mutableStateOf(ProfileData())

    var posts = listOf<AuthorPost>()
    var postsState = mutableStateOf(posts)

    var likedPosts = listOf<AuthorPost>()
    var likedPostsState = mutableStateOf(likedPosts)

    init {
        getProfileData()
        getPosts()
    }

    private fun getProfileData() {
        viewModelScope.launch {
            state.value = repository.getProfileData()
        }
    }

    private fun getPosts() {
        viewModelScope.launch {
            val posts = postRepository.getMyPosts()

            val postsWithAuthorInfo = posts.map { post ->
                if (post.author.isNotEmpty()) {
                    val authorProfile = postRepository.getProfileDataById(post.author)
                    AuthorPost(post, authorProfile)
                } else {
                    AuthorPost(post, ProfileData())
                }
            }
            postsState.value = postsWithAuthorInfo
        }
    }

    // Todo: Boilerplate in FeedViewModel
    fun navigateToProfileOrDetail(
        navController: NavController,
        userIdToNavigate: String,
    ) {
        if (coreRepository.getCurrentUserID() == userIdToNavigate) {
            // Navigate to the user's own profile
            navController.navigate(Screens.ProfileScreen.route)
        } else {
            // Navigate to the detail screen for another user
            navController.navigate(
                "detail/{user}"
                    .replace(
                        oldValue = "{user}",
                        newValue = userIdToNavigate,
                    ),
            )
        }
    }

    fun logOut() {
        auth.signOut()
    }
}