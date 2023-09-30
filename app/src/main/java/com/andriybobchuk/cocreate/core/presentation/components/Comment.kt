package com.andriybobchuk.cocreate.core.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.andriybobchuk.cocreate.R
import com.andriybobchuk.cocreate.ui.theme.*

@Composable
fun Comment(
    imageUrl: String,
    username: String,
    timestamp: String,
    commentText: String,
    modifier: Modifier = Modifier,
    onProfileClick:() -> Unit,
    onDeleteClick: () -> Unit,
    isOwnComment: Boolean,
) {
    var isClicked by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onProfileClick)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.Top
    ) {
        Avatar(
            radius = 38.dp,
            font = 14.sp,
            avatarUrl = imageUrl,
            name = username
        )

        Spacer(modifier = Modifier.width(12.dp))

        // Comment content
        Row {
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
                    Spacer(modifier = Modifier.width(4.dp))
                    // Timestamp in normal
                    Text(
                        text = "on $timestamp",
                        color = typo_gray100,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
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
            if (isOwnComment) {
                IconButton(
                    onClick = {
                        onDeleteClick()
                    },
                    //odifier = Modifier.padding(end = 10.dp),
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
}
