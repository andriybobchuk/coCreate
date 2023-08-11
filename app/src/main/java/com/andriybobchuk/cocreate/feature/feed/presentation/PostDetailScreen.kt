package com.andriybobchuk.cocreate.feature.feed.presentation

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.andriybobchuk.cocreate.R
import com.andriybobchuk.cocreate.core.data.repository.CoreRepository
import com.andriybobchuk.cocreate.core.presentation.components.Comment
import com.andriybobchuk.cocreate.core.presentation.components.post.Post
import com.andriybobchuk.cocreate.core.presentation.components.post.components.FeedbackButton
import com.andriybobchuk.cocreate.core.presentation.components.post.components.TagSection
import com.andriybobchuk.cocreate.feature.profile.domain.model.ProfileData
import com.andriybobchuk.cocreate.feature.profile.presentation.ProfileViewModel
import com.andriybobchuk.cocreate.ui.theme.*
import com.andriybobchuk.navigation.Screens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@Composable
fun PostDetailScreen(
    navController: NavController,
    viewModel: PostDetailViewMOdel = hiltViewModel(),
    id: String
) {
    viewModel.getPostById(id)
    val postData = viewModel.postDataState.value

    viewModel.getCommentsByPostId(id)
    val comments = viewModel.commentsState.value

    Column(
        modifier = Modifier
            .background(background_gray100)
            .fillMaxSize()
    ) {
        // Header
        Row(
            modifier = Modifier
                .height(50.dp)
                .background(white),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.padding(start = 10.dp),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow),
                    contentDescription = "Back"
                )
            }
            Text(
                text = "Details",
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                color = title_black,
                fontFamily = poppins,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1f)
            )
        }
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Box(
                Modifier
                    .background(white)
            ) {
                // Post
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(start = 16.dp, end = 16.dp, top = (0 + 5).dp, bottom = 6.dp)
                        .background(white),
                    //horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        Modifier.background(white)
                        //verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(postData.postAuthor.avatar),
                            contentDescription = "Avatar",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(41.dp)
                                .clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.width(11.dp))
                        Column(
                            modifier = Modifier
                                .wrapContentHeight()
                                .weight(1f)
                        ) {
                            Text(
                                text = postData.postAuthor.name,
                                fontSize = 14.sp,
                                fontFamily = poppins,
                            )
                            Text(
                                text = postData.postBody.published,
                                fontSize = 12.sp,
                                fontFamily = poppins,
                                color = typo_gray100,
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = postData.postBody.title,
                        fontSize = 15.sp,
                        fontFamily = poppins,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = postData.postBody.desc,
                        fontSize = 14.sp,
                        fontFamily = poppins,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    TagSection(
                        sectionName = "Tags",
                        tags = postData.postBody.tags
                    )
                    Divider(
                        modifier = Modifier.padding(top = 10.dp),
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.05f),
                        thickness = 0.4.dp
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .background(white)
                    ) {
                        FeedbackButton(
                            painter = painterResource(id = R.drawable.ic_like),
                            isHighlighted = true,
                            count = postData.postBody.likes,
                            onClick = {})
                        FeedbackButton(
                            painter = painterResource(id = R.drawable.ic_messages),
                            count = postData.postBody.comments,
                            onClick = {})
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(background_gray100)
                    .padding(vertical = 10.dp)
            ) {
                comments.forEach { comment ->
                    Comment(
                        imageUrl = comment.authorData.avatar,
                        username = comment.authorData.name,
                        timestamp = comment.body.published,
                        commentText = comment.body.desc,
                        onProfileClick = {
                            navController.navigate(
                                "detail/{user}"
                                    .replace(
                                        oldValue = "{user}",
                                        newValue = comment.authorData.uid
                                    )
                            )
                        }
                    )
                }
            }
        }
    }
}
