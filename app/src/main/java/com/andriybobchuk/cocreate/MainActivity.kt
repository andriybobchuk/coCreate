package com.andriybobchuk.cocreate

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.andriybobchuk.cocreate.core.data.repository.CoreRepository
import com.andriybobchuk.cocreate.navigation.BottomNavItem
import com.andriybobchuk.cocreate.navigation.BottomNavigationBar
import com.andriybobchuk.cocreate.navigation.NavigationGraph
import com.andriybobchuk.cocreate.ui.theme.CoCreateTheme
import com.andriybobchuk.cocreate.util.ccLog
import com.andriybobchuk.navigation.Screens
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity(): ComponentActivity() {

    @Inject lateinit var coreRepository: CoreRepository
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CoCreateTheme {

                // TODO: Fix this ugly way of setting the status bar in the future
                window?.setStatusBarColor(Color.White.toArgb())
                window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

                var startDestination = ""
                if(coreRepository.getCurrentUserID() != "") {
                    startDestination = Screens.ProfileScreen.route // TODO("Change to Screens.RegisterScreen.route in startDestination in RELEASE MODE")
                    ccLog.e("Entry", "User logged in, userID: " + coreRepository.getCurrentUserID())
                } else {
                    startDestination = Screens.LoginScreen.route // TODO("Change to Screens.RegisterScreen.route in startDestination in RELEASE MODE")
                    ccLog.e("Entry", "User not logged in")
                }

                val navController = rememberNavController()
                NavigationGraph(navController = navController, startDestination = startDestination)

                val currentBackStackEntry by navController.currentBackStackEntryAsState()

                // Create a flag to determine whether to show the bottom navigation bar
                val showBottomNavBar = currentBackStackEntry?.destination?.route in listOf(
                    Screens.FeedScreen.route,
                    Screens.MessagesScreen.route,
                    Screens.ProfileScreen.route,
                    Screens.CollaboratorsScreen.route,
                    Screens.ConversationScreen.route
                )

                Scaffold(
                    bottomBar = {
                        ccLog.e("MainActivity", "Current Route: ${navController.currentDestination?.route}")

                        if (showBottomNavBar) {
                            BottomNavigationBar(
                                items = listOf(
                                    BottomNavItem(
                                        name = "Feed",
                                        route = Screens.FeedScreen.route,
                                        icon = ImageVector.vectorResource(id = R.drawable.ic_myposts)
                                    ),
                                    BottomNavItem(
                                        name = "Collaborators",
                                        route = Screens.CollaboratorsScreen.route,
                                        icon = ImageVector.vectorResource(id = R.drawable.ic_people)
                                    ),
                                    BottomNavItem(
                                        name = "Messages",
                                        route = Screens.MessagesScreen.route,
                                        icon = ImageVector.vectorResource(id = R.drawable.ic_messages),
                                        badgeCount = 23
                                    ),
                                    BottomNavItem(
                                        name = "Profile",
                                        route = Screens.ProfileScreen.route,
                                        icon = ImageVector.vectorResource(id = R.drawable.ic_profile1)
                                    ),
                                ),
                                navController = navController,
                                onItemClick = { screen ->
                                    navController.navigate(screen.route)
                                }
                            )
                        }
                    }
                ) { innerPadding ->
                    // Apply the padding globally to the whole BottomNavScreensController, so that bottom nav bar does not overlap the content
                    Box(modifier = Modifier.padding(innerPadding)) {
                        if(coreRepository.getCurrentUserID() != "") {
                            startDestination = Screens.ProfileScreen.route // TODO("Change to Screens.RegisterScreen.route in startDestination in RELEASE MODE")
                            ccLog.e("Entry", "User logged in, userID: " + coreRepository.getCurrentUserID())
                        } else {
                            startDestination = Screens.LoginScreen.route // TODO("Change to Screens.RegisterScreen.route in startDestination in RELEASE MODE")
                            ccLog.e("Entry", "User not logged in")
                        }
                        NavigationGraph(navController = navController, startDestination = startDestination)
                    }
                }
            }
        }
    }
}