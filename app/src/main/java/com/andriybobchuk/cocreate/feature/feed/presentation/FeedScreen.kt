package com.andriybobchuk.cocreate.feature.feed.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.andriybobchuk.cocreate.R
import com.andriybobchuk.cocreate.core.presentation.components.post.Post
import com.andriybobchuk.cocreate.core.presentation.components.search_bar.SearchBar
import com.andriybobchuk.cocreate.core.presentation.screens.SomeonesProfileViewModel
import com.andriybobchuk.cocreate.ui.theme.*
import com.andriybobchuk.navigation.Screens
import androidx.compose.runtime.collectAsState

@Composable
fun FeedScreen(
    navController: NavController,
    viewModel: FeedViewModel = hiltViewModel(),
) {
    val posts = viewModel.state.value

//    // State variable to trigger recomposition when a like action occurs
//    var likeActionState by remember { mutableStateOf(false) }

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
                    text = "Ideas \uD83D\uDCA1",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = title_black,
                    fontFamily = poppins,
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .weight(1f)
                )
                IconButton(
                    onClick = { navController.navigate(Screens.AddPostScreen.route) },
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 50.dp)
                .background(background_gray100)
        ) {
            items(posts) { post ->
                Post(
                    ownerAvatar = post.postAuthor.avatar,
                    ownerName = post.postAuthor.name,
                    publishedTime = post.postBody.published,
                    title = post.postBody.title,
                    contentText = post.postBody.desc,
                    tags = post.postBody.tags,
                    likes = post.postBody.likes,
                    comments = post.postBody.comments,
                    isLiked = post.postBody.isLiked,
                    onLikeClick = {
                        viewModel.likeOrUnlikePost(post.postBody.uid)
                    },
                    onCommentClick = {
                        System.out.println("Comment clicked")
                        navController.navigate(
                            "postDetail/{user}"
                                .replace(
                                    oldValue = "{user}",
                                    newValue = post.postBody.uid
                                )
                        )
                    },
                    onReadMoreClick = {
                        navController.navigate(
                            "postDetail/{user}"
                                .replace(
                                    oldValue = "{user}",
                                    newValue = post.postBody.uid
                                )
                        )
                    }
                )
            }
        }
    }
}