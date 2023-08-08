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
import com.andriybobchuk.cocreate.feature.feed.presentation.AddPostScreen
import com.andriybobchuk.cocreate.feature.feed.presentation.PostDetailScreen
import com.andriybobchuk.cocreate.feature.profile.presentation.ProfileScreen
import com.andriybobchuk.cocreate.feature.profile.presentation.ProfileDetailScreen
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
        // Auth
        composable(route = Screens.LoginScreen.route) {
            LoginScreen(navController)
        }
        composable(route = Screens.RegisterScreen.route) {
            RegisterScreen(navController)
        }

        // Feed
        composable(route = Screens.FeedScreen.route) {
            FeedScreen(navController)
        }
        composable(route = Screens.AddPostScreen.route) {
            AddPostScreen(navController)
        }
        composable(route = Screens.PostDetailScreen.route) {
            PostDetailScreen(navController)
        }

        composable(route = Screens.MyPostsScreen.route) {
            MyPostsScreen(navController)
        }
        composable(route = Screens.MessagesScreen.route) {
            MessagesScreen(navController)
        }

        // Profile
        composable(route = Screens.ProfileScreen.route) {
            ProfileScreen(navController)
        }
        composable(route = Screens.ProfileDetailScreen.route) {
            ProfileDetailScreen(navController)
        }
    }

}
