package com.andriybobchuk.cocreate.core.presentation.components.post

import androidx.compose.foundation.Image
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.andriybobchuk.cocreate.R
import com.andriybobchuk.cocreate.core.Constants
import com.andriybobchuk.cocreate.core.presentation.components.post.components.FeedbackButton
import com.andriybobchuk.cocreate.core.presentation.components.post.components.TagSection
import com.andriybobchuk.cocreate.ui.theme.accent
import com.andriybobchuk.cocreate.ui.theme.poppins
import com.andriybobchuk.cocreate.ui.theme.typo_gray100
import com.andriybobchuk.cocreate.ui.theme.typo_gray200
import com.andriybobchuk.navigation.Screens

@Composable
fun Post(
    navController: NavController,
    ownerAvatar: Painter,
    ownerName: String,
    publishedTime: String,
    contentText: String,
    tags: List<String>,
    onLikeClick: () -> Unit,
    onCommentClick: () -> Unit,
) {
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
                //verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = ownerAvatar,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(41.dp)
                        .clip(CircleShape)
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
                Column(Modifier.wrapContentHeight()) {
                    IconButton(
                        onClick = { },
                        modifier = Modifier.padding(end = 10.dp),
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_edit),
                            tint = typo_gray100,
                            contentDescription = "Edit"
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "AI based recommender system",
                fontSize = 14.sp,
                fontFamily = poppins,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = contentText,
                fontSize = 14.sp,
                fontFamily = poppins,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = "Read More",
                fontSize = 14.sp,
                fontFamily = poppins,
                color = accent,
                modifier = Modifier
                    .clickable(onClick = { navController.navigate(
                        Screens.PostDetailScreen.route) })
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
                FeedbackButton(painter = painterResource(id = R.drawable.ic_like), isHighlighted = true, count = 12, onClick = {})
                FeedbackButton(painter = painterResource(id = R.drawable.ic_messages), count = 4, onClick = { navController.navigate(
                    Screens.PostDetailScreen.route) })

            }
        }
    }
}