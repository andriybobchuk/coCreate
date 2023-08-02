package com.andriybobchuk.cocreate.feature.feed.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.andriybobchuk.cocreate.R
import com.andriybobchuk.cocreate.core.presentation.components.post.Post
import com.andriybobchuk.cocreate.ui.theme.*

@Composable
fun FeedScreen(
    navController: NavController
) {
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
                    text = "Ideas",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = title_black,
                    fontFamily = poppins,
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .weight(1f)
                )
                IconButton(
                    onClick = { /* navigate to add post screen */ },
                    modifier = Modifier.padding(end = 0.dp),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_add),
                        contentDescription = "Add post"
                    )
                }
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
                .padding(top = 54.dp)
                .verticalScroll(rememberScrollState())
                .background(background_gray100)
        ) {
            // List of posts can be added here
            Post(
                ownerAvatar = rememberAsyncImagePainter(model = "https://andriybobchuk.com/images/about.jpg"),
                ownerName = "John Kekho",
                publishedTime = "5 decades ago",
                contentText = "Hello ladies and gentlemen, this is my very first post on coCreate!\n\nHello ladies and gentlemen, this is my very first post on coCreate!Hello ladies and gentlemen, this is my very first post on coCreate!Hello ladies and gentlemen, this is my very first post on coCreate!",
                tools = listOf(
                    "Android Studio",
                    "Figma",
                    "Notion",
                    "Clion",
                    "Visual Studio",
                    "MatLab"
                ),
                skills = listOf("hardworkpaysoff", "hustle", "whatever it takes"),
                onLikeClick = { /*TODO*/ },
                onCommentClick = { /*TODO*/ }
            )
            Post(
                ownerAvatar = rememberAsyncImagePainter(model = "https://andriybobchuk.com/images/about.jpg"),
                ownerName = "John Kekho",
                publishedTime = "5 decades ago",
                contentText = "Hello ladies and gentlemen, this is my very first post on coCreate!",
                tools = listOf(
                    "Android Studio",
                    "Figma",
                    "Notion",
                    "Clion",
                    "Visual Studio",
                    "MatLab"
                ),
                skills = listOf("hardworkpaysoff", "hustle", "whatever it takes"),
                onLikeClick = { /*TODO*/ },
                onCommentClick = { /*TODO*/ }
            )
            Post(
                ownerAvatar = rememberAsyncImagePainter(model = "https://andriybobchuk.com/images/about.jpg"),
                ownerName = "John Kekho",
                publishedTime = "5 decades ago",
                contentText = "Hello ladies and gentlemen, this is my very first post on coCreate!\nHello ladies and gentlemen, this is my very first post on coCreate!Hello ladies and gentlemen, this is my very first post on coCreate!",
                tools = listOf(
                    "Android Studio",
                    "Figma",
                    "Notion",
                    "Clion",
                    "Visual Studio",
                    "MatLab"
                ),
                skills = listOf("hardworkpaysoff", "hustle", "whatever it takes"),
                onLikeClick = { /*TODO*/ },
                onCommentClick = { /*TODO*/ }
            )
        }
    }
}

@Composable
fun SearchBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onCancelSearch: () -> Unit
) {
    Row(
        modifier = Modifier
            .height(50.dp)
            .background(white),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .padding(start = 10.dp, end = 8.dp)
                .background(background_gray200, RoundedCornerShape(14.dp))
                .padding(8.dp)
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = "Search",
                tint = typo_gray100
            )
            if (searchQuery.isEmpty()) {
                Text(
                    text = "Search tools, profession, city",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            BasicTextField(
                value = searchQuery,
                onValueChange = { newValue -> onSearchQueryChange(newValue) },
                textStyle = TextStyle(color = typo_gray200, fontSize = 16.sp),
                singleLine = true,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .fillMaxWidth()
            )
        }

        IconButton(
            onClick = { onCancelSearch() },
            modifier = Modifier.padding(end = 16.dp),
        ) {
            Text(
                text = "Cancel",
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
            )
        }
    }
}