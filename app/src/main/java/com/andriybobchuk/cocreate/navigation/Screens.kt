package com.andriybobchuk.navigation

sealed class Screens(val route: String) {
    object LoginScreen : Screens(route = "Login_Screen")
    object RegisterScreen : Screens(route = "Register_Screen")


}