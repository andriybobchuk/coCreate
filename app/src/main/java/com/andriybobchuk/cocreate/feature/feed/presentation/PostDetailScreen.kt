package com.andriybobchuk.cocreate.feature.feed.presentation

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
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
import com.andriybobchuk.cocreate.core.domain.model.AuthorPost
import com.andriybobchuk.cocreate.core.presentation.components.Avatar
import com.andriybobchuk.cocreate.core.presentation.components.Comment
import com.andriybobchuk.cocreate.core.presentation.components.post.Post
import com.andriybobchuk.cocreate.core.presentation.components.post.components.FeedbackButton
import com.andriybobchuk.cocreate.core.presentation.components.post.components.TagSection
import com.andriybobchuk.cocreate.feature.profile.domain.model.ProfileData
import com.andriybobchuk.cocreate.feature.profile.presentation.ProfileViewModel
import com.andriybobchuk.cocreate.ui.theme.*
import com.andriybobchuk.cocreate.util.toEpochMillis
import com.andriybobchuk.navigation.Screens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@Composable
fun PostDetailScreen(
    navController: NavController,
    viewModel: PostDetailViewModel = hiltViewModel(),
    id: String
) {
    viewModel.getPostById(id)
    val postData = viewModel.postDataState.value

    val state by viewModel.state.collectAsState()
    LaunchedEffect(key1 = true) {
        viewModel.getCommentsByPostId(id)
    }

    val hasCommentBeenAdded by viewModel.hasCommentBeenAdded.collectAsState()
    LaunchedEffect(key1 = hasCommentBeenAdded) {
        if(hasCommentBeenAdded) {
            viewModel.getCommentsByPostId(id)
        }
    }

    Column(
        modifier = Modifier
            .background(background_gray100)
            .fillMaxSize()
    ) {
        Header(navController)
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .background(background_gray100)
            // .padding(vertical = 10.dp)
        ) {
            item {
                PostBody(postData)
            }
            items(state.comments.sortedBy { toEpochMillis(it.body.published) }) { comment ->
                Comment(
                    imageUrl = comment.authorData.avatar,
                    username = comment.authorData.name,
                    timestamp = comment.body.published,
                    commentText = comment.body.desc,
                    onProfileClick = {
                        viewModel.navigateToProfileOrDetail(navController, comment.authorData.uid)
                    },
                    onDeleteClick = {
                        viewModel.removeComment(comment.body.uid)
                    },
                    isOwnComment = viewModel.isOwnComment(comment.authorData.uid)
                )
            }
        }
        InputField(viewModel, id)
    }
}

@Composable
fun Header(navController: NavController) {
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
}

@Composable
fun PostBody(postData: AuthorPost) {
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
                Avatar(
                    radius = 41.dp,
                    font = 16.sp,
                    avatarUrl = postData.postAuthor.avatar,
                    name = postData.postAuthor.name
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
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(backgroundColor = background_gray100),
                    shape = CircleShape,
                    elevation = ButtonDefaults.elevation(0.dp),
                    modifier = Modifier
                        .height(36.dp)
                        .padding(end = 8.dp),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_star),
                        contentDescription = null,
                        tint = typo_gray200,
                        modifier = Modifier.size(16.dp)
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
        }
    }
}

@Composable
fun InputField(viewModel: PostDetailViewModel, postUid: String) {
    val commentMessageState = remember { mutableStateOf("") }
    //Spacer(modifier = Modifier.weight(1f))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(white)
            .padding(vertical = 4.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BasicTextField(
            value = commentMessageState.value,
            onValueChange = { newText -> commentMessageState.value = newText },
            textStyle = TextStyle(color = title_black, fontFamily = poppins),
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
                .border(1.dp, background_gray200, RoundedCornerShape(16.dp))
                .padding(8.dp)
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_send),
            contentDescription = "Send",
            tint = title_black,
            modifier = Modifier.clickable {
                val messageText = commentMessageState.value
                if (messageText.isNotBlank()) {
                    viewModel.addComment(
                        postUid = postUid,
                        desc = commentMessageState.value
                    )
                    commentMessageState.value = ""
                }
            }
        )
    }
}
