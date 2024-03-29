package com.andriybobchuk.cocreate.core.presentation.components.post

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andriybobchuk.cocreate.R
import com.andriybobchuk.cocreate.core.Constants
import com.andriybobchuk.cocreate.core.presentation.components.Avatar
import com.andriybobchuk.cocreate.core.presentation.components.post.components.FeedbackButton
import com.andriybobchuk.cocreate.core.presentation.components.post.components.TagSection
import com.andriybobchuk.cocreate.ui.theme.*

const val POST_TRIM_LENGTH = 300;
@Composable
fun Post(
    ownerAvatar: String,
    ownerName: String,
    publishedTime: String,
    title: String,
    contentText: String,
    tags: List<String>,
    isMine: Boolean,
    onSaveClick: () -> Unit,
    onCommentClick: () -> Unit,
    onReadMoreClick: () -> Unit,
    onEditClick: () -> Unit,
    onProfileClick: () -> Unit,
) {
   // val isLikedState = rememberUpdatedState(isLiked)

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
                modifier = Modifier.clickable { onProfileClick() }
                //verticalAlignment = Alignment.CenterVertically
            ) {
                Avatar(
                    radius = 41.dp,
                    font = 16.sp,
                    avatarUrl = ownerAvatar,
                    name = ownerName
                )

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
                    painter = painterResource(id = R.drawable.ic_star),
                    text = "Save",
                    onClick = {  }
                )
                FeedbackButton(
                    painter = painterResource(id = R.drawable.ic_messages),
                    text = "Comment",
                    onClick = { onCommentClick() }
                )

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