package com.andriybobchuk.cocreate.core.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.andriybobchuk.cocreate.ui.theme.poppins
import com.andriybobchuk.cocreate.ui.theme.typo_gray100
import com.andriybobchuk.cocreate.ui.theme.typo_gray200

@Composable
fun Comment(
    imageUrl: String,
    username: String,
    timestamp: String,
    commentText: String,
    modifier: Modifier = Modifier,
    onProfileClick:() -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onProfileClick)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.Top
    ) {
        // Avatar
        Image(
            painter = rememberAsyncImagePainter(imageUrl),
            contentDescription = "Avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.width(12.dp))

        // Comment content
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                //horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Name in bold
                Text(
                    text = username,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    fontFamily = poppins,
                )
                Spacer(modifier = Modifier.width(9.dp))
                // Timestamp in normal
                Text(
                    text = timestamp,
                    color = typo_gray100,
                    fontWeight = FontWeight.Normal,
                    fontSize = 13.sp,
                    fontFamily = poppins,
                )
            }
            // Comment text
            Text(
                text = commentText,
                modifier = Modifier
                    .fillMaxWidth(),
                maxLines = Int.MAX_VALUE, // Display all lines for long comments
                overflow = TextOverflow.Visible,
                fontFamily = poppins,
                style = TextStyle(fontSize = 13.sp),
                color = typo_gray200
            )
        }
    }
}