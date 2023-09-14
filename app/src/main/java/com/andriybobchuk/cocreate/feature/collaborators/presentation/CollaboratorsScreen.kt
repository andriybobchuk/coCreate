package com.andriybobchuk.cocreate.feature.collaborators.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.andriybobchuk.cocreate.R
import com.andriybobchuk.cocreate.core.domain.model.Person
import com.andriybobchuk.cocreate.core.presentation.components.post.Post
import com.andriybobchuk.cocreate.core.presentation.components.search_bar.SearchBar
import com.andriybobchuk.cocreate.feature.profile.presentation.ProfileViewModel
import com.andriybobchuk.cocreate.ui.theme.*
import com.andriybobchuk.navigation.Screens

enum class TabItem(val title: String) {
    TAB_1("Friends"),
    TAB_2("Requests"),
    TAB_3("Explore")
}

@Composable
fun MyPostsScreen(
    navController: NavController,
    viewModel: CollaboratorsViewModel = hiltViewModel()
) {
    val friendsList = viewModel.state.value
    val exploreList = viewModel.exploreState.value
    val requestList = viewModel.requestState.value

    var isSearchBarActive by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    var selectedTab by remember { mutableStateOf(TabItem.TAB_1) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        if (isSearchBarActive) {
            // Search input field with icon and hint
            SearchBar(
                searchQuery = searchQuery,
                onSearchQueryChange = { query -> searchQuery = query },
                onCancelSearch = {
                    isSearchBarActive = false
                    searchQuery = "" // Clear the search query when canceling
                }
            )
        } else {
            Row(
                modifier = Modifier
                    .height(50.dp)
                    .background(white),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Collaborators",
                    fontSize = 20.sp, fontWeight = FontWeight.Black, color = title_black, fontFamily = poppins,
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .weight(1f)
                )
                IconButton(
                    onClick = { isSearchBarActive = true },
                    modifier = Modifier.padding(end = 10.dp),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = "Search"
                    )
                }
            }
        }
        TabRow(
            selectedTabIndex = selectedTab.ordinal,
            modifier = Modifier.fillMaxWidth().padding(0.dp).background(white),
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    color = accent,
                    height = 2.dp,
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab.ordinal])
                )
            }
        ) {
            TabItem.values().forEachIndexed { index, tabItem ->
                Tab(
                    modifier = Modifier.background(white),
                    selected = selectedTab == tabItem,
                    onClick = {
                        selectedTab = tabItem
                        // Handle navigation or content update based on the selected tab
                    },
                    text = {
                        Text(
                            fontFamily = poppins,
                            fontSize = 14.sp,
                            text = tabItem.title,
                            color = if (selectedTab == tabItem) accent else typo_gray100
                        )
                    }
                )
            }
        }
        Divider(
            modifier = Modifier.padding(),
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.09f),
            thickness = 0.5.dp
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(background_gray100)
        ) {
            if(selectedTab == TabItem.TAB_1) {
                items(friendsList) { friend ->
                    Collaborator(
                        name = friend.name,
                        description = friend.position + " " + friend.city,
                        imageUrl = friend.avatar,
                        onProfileClick = {
                            navController.navigate(
                                "detail/{user}"
                                    .replace(
                                        oldValue = "{user}",
                                        newValue = friend.uid
                                    )
                            )
                        },
                        onMessageClick = {

                        }
                    )
                }
            } else if(selectedTab == TabItem.TAB_2) {
                items(requestList) { requestor ->
                    Requester(
                        name = requestor.name,
                        description = requestor.position + " " + requestor.city,
                        imageUrl = requestor.avatar,
                        onProfileClick = {
                            navController.navigate(
                                "detail/{user}"
                                    .replace(
                                        oldValue = "{user}",
                                        newValue = requestor.uid
                                    )
                            )
                        },
                        onAcceptClick = {
                            viewModel.approveFriend(requestor.uid)
                        }
                    )
                }
            } else {
                items(exploreList) { friend ->
                    Suggestion(
                        name = friend.name,
                        description = friend.position + " " + friend.city,
                        imageUrl = friend.avatar,
                        onProfileClick = {
                            navController.navigate(
                                "detail/{user}"
                                    .replace(
                                        oldValue = "{user}",
                                        newValue = friend.uid
                                    )
                            )
                        },
                        onRequestClick = {
                            viewModel.requestFriend(friend.uid)
                        }
                    )
                }
            }
        }
    }
}