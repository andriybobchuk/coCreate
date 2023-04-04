package com.andriybobchuk.cocreate.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.andriybobchuk.cocreate.feature.auth.presentation.use_case.login.LoginScreen
import com.andriybobchuk.cocreate.feature.auth.presentation.use_case.register.RegisterScreen
import com.andriybobchuk.navigation.Screens
import dagger.hilt.android.lifecycle.HiltViewModel

@Composable
fun NavigationGraph(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = Screens.RegisterScreen.route
    ) {
        composable(route = Screens.LoginScreen.route) {
            LoginScreen(navController)

        }
        composable(route = Screens.RegisterScreen.route) {
            RegisterScreen(navController)

        }
    }

}
