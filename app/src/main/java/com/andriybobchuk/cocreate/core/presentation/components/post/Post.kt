package com.andriybobchuk.cocreate.core.presentation.components.post

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.andriybobchuk.cocreate.R
import com.andriybobchuk.cocreate.core.Constants
import com.andriybobchuk.cocreate.core.presentation.components.post.components.FeedbackButton
import com.andriybobchuk.cocreate.core.presentation.components.post.components.TagSection
import com.andriybobchuk.cocreate.ui.theme.*
import com.andriybobchuk.navigation.Screens

const val POST_TRIM_LENGTH = 300;
@Composable
fun Post(
    ownerAvatar: String,
    ownerName: String,
    publishedTime: String,
    title: String,
    contentText: String,
    tags: List<String>,
    likes: Int,
    comments: Int,
    isLiked: Boolean,
    isMine: Boolean,
    onLikeClick: () -> Unit,
    onCommentClick: () -> Unit,
    onReadMoreClick: () -> Unit,
    onEditClick: () -> Unit,
) {
    val isLikedState = rememberUpdatedState(isLiked)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(Constants.CARD_ROUNDED_CORNERS))
    ) {
        Column(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 6.dp)
        ) {
            Row(
                modifier = Modifier.clickable {

                }
                //verticalAlignment = Alignment.CenterVertically
            ) {
                if (ownerAvatar != "") {
                    Image(
                        painter = rememberAsyncImagePainter(model = ownerAvatar),
                        contentDescription = "Avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(41.dp)
                            .clip(CircleShape)
                    )
                } else {
                    Box(
                        Modifier
                            .size(41.dp)
                            .clip(CircleShape)
                            .background(purple)
                    ) {
                        Text(
                            text = ownerName.take(1).toUpperCase(),
                            fontSize = 16.sp,
                            color = Color.White,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(11.dp))
                Column(
                    modifier = Modifier
                        .wrapContentHeight()
                        .weight(1f)) {
                    Text(
                        text = ownerName,
                        fontSize = 14.sp,
                        fontFamily = poppins,
                    )
                    Text(
                        text = publishedTime,
                        fontSize = 12.sp,
                        fontFamily = poppins,
                        color = typo_gray100,
                    )
                }
                if(isMine) {
                    Column(Modifier.wrapContentHeight()) {
                        IconButton(
                            onClick = {
                                onEditClick()
                            },
                            modifier = Modifier.padding(end = 10.dp),
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_edit),
                                tint = accent,
                                contentDescription = "Edit"
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = title,
                fontSize = 15.sp,
                fontFamily = poppins,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = if (contentText.length > POST_TRIM_LENGTH) contentText.substring(0, minOf(POST_TRIM_LENGTH, contentText.length)) + "..." else contentText,
                fontSize = 14.sp,
                fontFamily = poppins,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            TagSection(sectionName = "Tags", tags = tags)

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
            ) {
                FeedbackButton(
                    painter = painterResource(id = R.drawable.ic_like),
                    isHighlighted = isLikedState.value,
                    count = likes,
                    onClick = { onLikeClick() }
                )
                FeedbackButton(
                    painter = painterResource(id = R.drawable.ic_messages),
                    count = comments,
                    onClick = { onCommentClick() })

                // Read More button
                if (contentText.length > POST_TRIM_LENGTH) {
                    Button(
                        onClick = { onReadMoreClick() },
                        colors = ButtonDefaults.buttonColors(backgroundColor = background_gray100),
                        shape = CircleShape,
                        elevation = ButtonDefaults.elevation(0.dp),
                        modifier = Modifier
                            .height(36.dp)
                            .padding(end = 8.dp),
                    ) {
                        Text(
                            text = "Read More",
                            fontSize = 12.sp,
                            fontFamily = poppins,
                            color = accent,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }
                }
            }
        }
    }
}