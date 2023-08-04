package com.andriybobchuk.navigation

sealed class Screens(val route: String) {
    object LoginScreen : Screens(route = "Login_Screen")
    object RegisterScreen : Screens(route = "Register_Screen")
    object FeedScreen : Screens(route = "Feed_Screen")
    object MyPostsScreen : Screens(route = "MyPosts_Screen")
    object MessagesScreen : Screens(route = "Messages_Screen")
    object ProfileScreen : Screens(route = "Profile_Screen")


}