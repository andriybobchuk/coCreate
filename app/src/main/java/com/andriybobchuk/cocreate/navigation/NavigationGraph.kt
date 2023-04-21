package com.andriybobchuk.cocreate.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.andriybobchuk.cocreate.feature.auth.presentation.use_case.login.LoginScreen
import com.andriybobchuk.cocreate.feature.auth.presentation.use_case.register.RegisterScreen
import com.andriybobchuk.cocreate.feature.feed.presentation.FeedScreen
import com.andriybobchuk.cocreate.feature.messages.presentation.MessagesScreen
import com.andriybobchuk.cocreate.feature.collaborators.presentation.MyPostsScreen
import com.andriybobchuk.cocreate.feature.profile.presentation.ProfileScreen
import com.andriybobchuk.navigation.Screens

@Composable
fun NavigationGraph(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = Screens.LoginScreen.route) {
            LoginScreen(navController)

        }
        composable(route = Screens.RegisterScreen.route) {
            RegisterScreen(navController)

        }
        composable(route = Screens.FeedScreen.route) {
            FeedScreen(navController)

        }
        composable(route = Screens.MyPostsScreen.route) {
            MyPostsScreen(navController)

        }
        composable(route = Screens.MessagesScreen.route) {
            MessagesScreen(navController)

        }
        composable(route = Screens.ProfileScreen.route) {
            ProfileScreen(navController)

        }
    }

}
