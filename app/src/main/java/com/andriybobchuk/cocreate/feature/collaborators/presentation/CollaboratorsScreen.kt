package com.andriybobchuk.cocreate.feature.collaborators.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.andriybobchuk.cocreate.R
import com.andriybobchuk.cocreate.core.presentation.components.search_bar.SearchBar
import com.andriybobchuk.cocreate.ui.theme.*
import com.andriybobchuk.cocreate.util.ccLog
import com.andriybobchuk.cocreate.util.generateShortUserDescription

@Composable
fun MyPostsScreen(
    navController: NavController,
    viewModel: CollaboratorsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(key1 = true) {
        viewModel.getContacts()
        viewModel.getEveryone()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        if (state.isSearchActive) {
            // Search input field with icon and hint
            SearchBar(
                searchQuery = state.searchText,
                onSearchQueryChange = { viewModel.onSearchTextChange(it) },
                onCancelSearch = { viewModel.onToggleSearch() }
            )
        } else {
            Row(
                modifier = Modifier
                    .height(50.dp)
                    .background(white),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Saved Contacts",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = title_black,
                    fontFamily = poppins,
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .weight(1f),
                )
                IconButton(
                    onClick = { viewModel.onToggleSearch() },
                    modifier = Modifier.padding(end = 10.dp),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = "Search",
                    )
                }
            }
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(background_gray100),
        ) {
            items(
                items = state.contacts,
                key = { it.uid }
            ) { contact ->
                Collaborator(
                    name = contact.name,
                    description = generateShortUserDescription(contact.position, contact.city),
                    imageUrl = contact.avatar,
                    onProfileClick = { viewModel.navigateToProfileOrDetail(navController, contact.uid) },
                    onMessageClick = { viewModel.sendOrOpenExistingConversation(contact.uid, navController) },
                )
            }
        }
    }
}
