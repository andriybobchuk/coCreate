package com.andriybobchuk.cocreate.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.andriybobchuk.cocreate.core.presentation.screens.SomeonesProfileScreen
import com.andriybobchuk.cocreate.feature.auth.presentation.use_case.login.LoginScreen
import com.andriybobchuk.cocreate.feature.auth.presentation.use_case.register.RegisterScreen
import com.andriybobchuk.cocreate.feature.feed.presentation.FeedScreen
import com.andriybobchuk.cocreate.feature.messages.presentation.ConversationsScreen
import com.andriybobchuk.cocreate.feature.collaborators.presentation.MyPostsScreen
import com.andriybobchuk.cocreate.feature.feed.presentation.AddPostScreen
import com.andriybobchuk.cocreate.feature.feed.presentation.EditPostScreen
import com.andriybobchuk.cocreate.feature.feed.presentation.PostDetailScreen
import com.andriybobchuk.cocreate.feature.messages.presentation.PrivateChatScreen
import com.andriybobchuk.cocreate.feature.profile.presentation.ProfileScreen
import com.andriybobchuk.cocreate.feature.profile.presentation.ProfileEditScreen
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
        composable(route = "postDetail/{id}") { navBackStackEntry ->
            /* Extracting the id from the route */
            val id = navBackStackEntry.arguments?.getString("id")
            /* We check if it's not null */
            id?.let { it->
                PostDetailScreen(navController, id = id)
            }
        }
        composable(route = "postEdit/{id}") { navBackStackEntry ->
            /* Extracting the id from the route */
            val id = navBackStackEntry.arguments?.getString("id")
            /* We check if it's not null */
            id?.let { it->
                EditPostScreen(navController, id = id)
            }
        }

        composable(route = Screens.CollaboratorsScreen.route) {
            MyPostsScreen(navController)
        }
        composable(route = Screens.MessagesScreen.route) {
           ConversationsScreen(navController)
        }
        composable(route = "privateChat/{chatId}") { navBackStackEntry ->
            val id = navBackStackEntry.arguments?.getString("chatId")
            id?.let { it->
                PrivateChatScreen(navController, chatId = id)
            }
        }

//        composable(route = Screens.ConversationScreen.route) {
//            PrivateChatScreen(navController)
//        }

        // Profile
        composable(route = Screens.ProfileScreen.route) {
            ProfileScreen(navController)
        }
        composable(route = Screens.ProfileEditScreen.route) {
            ProfileEditScreen(navController)
        }
        composable(route = "detail/{id}") { navBackStackEntry ->
            /* Extracting the id from the route */
            val id = navBackStackEntry.arguments?.getString("id")
            /* We check if it's not null */
            id?.let { it->
                SomeonesProfileScreen(navController, id = id)
            }
        }
//        composable(route = "someonesProfileDetail/{id}") { navBackStackEntry ->
//            val id = navBackStackEntry.arguments?.getString("id")
//            id?.let { it->
//                ProfileDetailScreen(navController, id = id)
//            }
//        }
    }

}
