package com.andriybobchuk.navigation

sealed class Screens(val route: String) {
    object LoginScreen : Screens(route = "Login_Screen")
    object RegisterScreen : Screens(route = "Register_Screen")
    object FeedScreen : Screens(route = "Feed_Screen")
    object CollaboratorsScreen : Screens(route = "Collaborators_Screen")
    object MessagesScreen : Screens(route = "Messages_Screen")
    object ProfileScreen : Screens(route = "Profile_Screen")
    object ProfileEditScreen : Screens(route = "Profile_Edit_Screen")
    object AddPostScreen : Screens(route = "Add_Post_Screen")
    object ConversationScreen : Screens(route = "Conversation_Screen")
}