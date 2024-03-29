package com.andriybobchuk.cocreate.feature.collaborators.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.andriybobchuk.cocreate.R
import com.andriybobchuk.cocreate.core.presentation.components.Avatar
import com.andriybobchuk.cocreate.ui.theme.*

@Composable
fun Collaborator(name: String, description: String, imageUrl: String, onProfileClick:() -> Unit, onMessageClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onProfileClick),
        backgroundColor = Color.White,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 16.dp)
        ) {
            Avatar(
                radius= 45.dp,
                font = 16.sp,
                avatarUrl = imageUrl,
                name = name
            )
            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1f)
            ) {
                Text(
                    text = name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black,
                    fontFamily = poppins
                )
                Text(
                    text = description,
                    fontSize = 13.sp,
                    color = Color.Gray,
                    fontFamily = poppins
                )
            }
            IconButton(
                modifier =
                Modifier
                    .width(28.dp)
                    .height(28.dp)
                    .padding(end = 10.dp),
                onClick = { onMessageClick() }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_messages),
                    tint = accent,
                    contentDescription = "Send Message"
                )
            }
        }
    }
}