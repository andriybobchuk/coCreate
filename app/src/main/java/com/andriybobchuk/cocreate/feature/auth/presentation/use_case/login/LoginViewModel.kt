package com.andriybobchuk.cocreate.feature.auth.presentation.use_case.login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.andriybobchuk.cocreate.feature.auth.data.repository.AuthRepository
import com.andriybobchuk.cocreate.feature.auth.data.repository.AuthRepositoryImpl
import com.andriybobchuk.cocreate.util.Resource
import com.andriybobchuk.navigation.Screens
import com.google.firebase.auth.AuthCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    val _loginState = Channel<LoginState>()
    val loginState = _loginState.receiveAsFlow()

    fun loginUser(email: String, password: String, navController: NavController) = viewModelScope.launch {
        repository.loginUser(email, password).collect { result ->
            when (result) {
                is Resource.Success -> {
                    _loginState.send(LoginState(isSuccess = "Logged in successfully"))
                    navController.navigate(Screens.ProfileScreen.route)
                }
                is Resource.Loading -> {
                    _loginState.send(LoginState(isLoading = true))
                }
                is Resource.Error -> {
                    _loginState.send(LoginState(isError = result.message))
                }
            }
        }
    }

    val _googleLoginState = mutableStateOf(GoogleLoginState())
    val googleLoginState: State<GoogleLoginState> = _googleLoginState

    fun googleLogin(credential: AuthCredential) = viewModelScope.launch {
        repository.googleLogin(credential).collect { result ->
            when (result) {
                is Resource.Success -> {
                    _googleLoginState.value = GoogleLoginState(isSuccess = result.data)
                }
                is Resource.Loading -> {
                    _googleLoginState.value = GoogleLoginState(isLoading = true)
                }
                is Resource.Error -> {
                    _googleLoginState.value = GoogleLoginState(isError = result.message!!)
                }
            }
        }
    }

    val _facebookLoginState = mutableStateOf(FacebookLoginState())
    val facebookLoginState: State<FacebookLoginState> = _facebookLoginState

    fun facebookLogin(credential: AuthCredential) = viewModelScope.launch {
        repository.googleLogin(credential).collect { result ->
            when (result) {
                is Resource.Success -> {
                    _facebookLoginState.value = FacebookLoginState(isSuccess = result.data)
                }
                is Resource.Loading -> {
                    _facebookLoginState.value = FacebookLoginState(isLoading = true)
                }
                is Resource.Error -> {
                    _facebookLoginState.value = FacebookLoginState(isError = result.message!!)
                }
            }
        }
    }

}