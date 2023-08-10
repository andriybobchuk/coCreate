package com.andriybobchuk.cocreate.feature.collaborators.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
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
import com.andriybobchuk.cocreate.ui.theme.background_gray100
import com.andriybobchuk.cocreate.ui.theme.poppins
import com.andriybobchuk.cocreate.ui.theme.title_black
import com.andriybobchuk.cocreate.ui.theme.white
import com.andriybobchuk.navigation.Screens

@Composable
fun MyPostsScreen(
    navController: NavController,
    viewModel: CollaboratorsViewModel = hiltViewModel()
) {
    val friendsList = viewModel.state.value

    var isSearchBarActive by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    Box(
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
                    text = "Collaborators \uD83D\uDE4B\u200D♂️",
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 50.dp)
                .background(background_gray100)
        ) {
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
                    }) {
                }
            }
        }
    }
}