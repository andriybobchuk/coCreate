package com.andriybobchuk.cocreate.core.presentation.components.post

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andriybobchuk.cocreate.core.Constants
import com.andriybobchuk.cocreate.core.presentation.components.post.components.FeedbackButton
import com.andriybobchuk.cocreate.core.presentation.components.post.components.TagSection
import com.andriybobchuk.cocreate.ui.theme.poppins
import com.andriybobchuk.cocreate.ui.theme.typo_gray100

@Composable
fun Post(
    ownerAvatar: Painter,
    ownerName: String,
    publishedTime: String,
    contentText: String,
    tools: List<String>,
    skills: List<String>,
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
                        .size(42.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(11.dp))
                Column(Modifier.wrapContentHeight()) {
                    Text(
                        text = ownerName,
                        fontSize = 15.sp,
                        fontFamily = poppins,
                    )
                    Text(
                        text = publishedTime,
                        fontSize = 12.sp,
                        fontFamily = poppins,
                        color = typo_gray100,
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = contentText,
                fontSize = 14.sp,
                fontFamily = poppins,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            TagSection(sectionName = "Tools", tags = tools)

            Spacer(modifier = Modifier.height(16.dp))

            TagSection(sectionName = "Skills", tags = skills)

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
                FeedbackButton(imageVector = Icons.Default.Favorite, isHighlighted = true, count = 12, onClick = {})
                FeedbackButton(imageVector = Icons.Default.Edit, count = 0, onClick = {})

            }
        }
    }
}