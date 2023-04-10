package com.andriybobchuk.cocreate

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.andriybobchuk.cocreate.core.data.repository.CoreRepository
import com.andriybobchuk.cocreate.navigation.BottomNavItem
import com.andriybobchuk.cocreate.navigation.BottomNavigationBar
import com.andriybobchuk.cocreate.navigation.NavigationGraph
import com.andriybobchuk.cocreate.ui.theme.CoCreateTheme
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
                val navController = rememberNavController()

                var startDestination = ""
                if(coreRepository.getCurrentUserID() != "") {
                    startDestination = Screens.ProfileScreen.route // TODO("Change to Screens.RegisterScreen.route in startDestination in RELEASE MODE")
                } else {
                    startDestination = Screens.LoginScreen.route // TODO("Change to Screens.RegisterScreen.route in startDestination in RELEASE MODE")
                }
                //val currentDestination = startDestination
                NavigationGraph(navController = navController, startDestination = startDestination)
                //TODO("Fix this severe architectural problem with calling NavitagionGraph twice")

                Scaffold(
                    bottomBar = {
                        if (navController.currentDestination?.route in listOf(
                                Screens.FeedScreen.route,
                                Screens.MessagesScreen.route,
                                Screens.ProfileScreen.route
                            )) {
                            BottomNavigationBar(
                                items = listOf(
                                    BottomNavItem(
                                        name = "Feed",
                                        route = Screens.FeedScreen.route,
                                        icon = ImageVector.vectorResource(id = R.drawable.ic_feed)
                                    ),
                                    BottomNavItem(
                                        name = "Ideas",
                                        route = Screens.MyPostsScreen.route,
                                        icon = ImageVector.vectorResource(id = R.drawable.ic_myposts)
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
                                        icon = ImageVector.vectorResource(id = R.drawable.ic_profile)
                                    ),
                                ),
                                navController = navController,
                                onItemClick = { screen ->
                                    navController.navigate(screen.route)
                                }
                            )
                        }
                    }
                ) {
                    NavigationGraph(navController = navController, startDestination = startDestination)
                }
            }
        }
    }
}