package com.andriybobchuk.cocreate.feature.messages.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.andriybobchuk.cocreate.R
import com.andriybobchuk.cocreate.core.presentation.components.search_bar.SearchBar
import com.andriybobchuk.cocreate.ui.theme.background_gray100
import com.andriybobchuk.cocreate.ui.theme.poppins
import com.andriybobchuk.cocreate.ui.theme.title_black
import com.andriybobchuk.cocreate.ui.theme.white
import com.andriybobchuk.cocreate.util.formatShortTimeAgo

val MESSAGE_TRIM_LENGTH = 29

@Composable
fun ConversationsScreen(
    navController: NavController,
    viewModel: ConversationViewModel = hiltViewModel(),
) {
    val conversations = viewModel.state.value

    var isSearchBarActive by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

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
                    text = "Conversations",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = title_black,
                    fontFamily = poppins,
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding()
                .background(background_gray100),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(background_gray100)
            ) {
                items(conversations) { convo ->
                    ConversationItem(
                        navController = navController,
                        avatar = convo.recipientData.avatar,
                        name = convo.recipientData.name,
                        lastMessage = if (convo.lastMessage.content.length > MESSAGE_TRIM_LENGTH) convo.lastMessage.content.substring(0, minOf(MESSAGE_TRIM_LENGTH, convo.lastMessage.content.length)) + "..." else convo.lastMessage.content,
                        time = formatShortTimeAgo(convo.lastMessage.time),
                        isRead = convo.convoData.isRead,
                        onClick = {
                            navController.navigate(
                                "privateChat/{chatId}"
                                    .replace(
                                        oldValue = "{chatId}",
                                        newValue = convo.convoData.uid
                                    )
                            )
                            viewModel.markConversationAsRead(convo.convoData.uid)
                        }
                    )
                }
            }
        }
    }
}