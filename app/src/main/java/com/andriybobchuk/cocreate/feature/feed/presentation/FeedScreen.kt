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
import com.andriybobchuk.cocreate.core.presentation.ShimmerListItem

@Composable
fun FeedScreen(
    navController: NavController,
    viewModel: FeedViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(key1 = true) {
        viewModel.loadPosts()
        //isLoading = false
    }

    Box(
        modifier = Modifier.fillMaxSize()
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
                    onClick = {
                        viewModel.onToggleSearch()
                        },
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
            items(
                items = state.posts,
                key = { it.postBody.uid }
            ) { post ->

                ShimmerListItem(
                    isLoading = state.isLoading,
                    contentAfterLoading = {
                        var isLiked by remember { mutableStateOf(post.postBody.isLiked) }
                        var likes by remember { mutableStateOf(post.postBody.likes) }
                        Post(
                            ownerAvatar = post.postAuthor.avatar,
                            ownerName = post.postAuthor.name,
                            publishedTime = post.postBody.published,
                            title = post.postBody.title,
                            contentText = post.postBody.desc,
                            tags = post.postBody.tags,
                            isMine = post.postBody.isMine,
                            onSaveClick = {
                                //viewModel.likeOrUnlikePost(post.postBody.uid)
                                isLiked = !isLiked
                                if(isLiked) {
                                    likes++
                                } else {
                                    likes--
                                }
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
                            },
                            onEditClick = {
                                if(post.postBody.isMine) {
                                    navController.navigate(
                                        "postEdit/{post}"
                                            .replace(
                                                oldValue = "{post}",
                                                newValue = post.postBody.uid
                                            )
                                    )
                                }
                            }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }
    }
}