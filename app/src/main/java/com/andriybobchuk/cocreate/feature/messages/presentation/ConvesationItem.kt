package com.andriybobchuk.cocreate.feature.messages.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.andriybobchuk.cocreate.ui.theme.*

@Composable
fun ConversationItem() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = {}),
        backgroundColor = white,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 16.dp)
        ) {
            if ("imageUrl" != "") {
                Image(
                    painter = rememberAsyncImagePainter("https://andriybobchuk.com/images/about.jpg"),
                    contentDescription = "Cover Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(45.dp)
                        .clip(CircleShape)
                )
            } else {
                Box(
                    Modifier
                        .size(45.dp)
                        .clip(CircleShape)
                        .background(purple),
                ) {
                    Text(
                        text = "Andrii".take(1).toUpperCase(),
                        fontSize = 16.sp,
                        color = white,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1f)
            ) {
                Text(
                    text = "Jeffrey Bezos",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = title_black,
                    fontFamily = poppins
                )
                Text(
                    text = "Hey, nice to meet you! Could yo...",
                    fontSize = 13.sp,
                    color = typo_gray100,
                    fontFamily = poppins,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp)) // Add space between message excerpt and timestamp
            Text(
                text = "1h ago", // Replace with the actual timestamp
                fontSize = 12.sp,
                color = typo_gray100,
                fontFamily = poppins
            )
            Spacer(modifier = Modifier.width(16.dp))
            // Add a blue circle indicating an unread message
            Spacer(
                modifier = Modifier
                    .size(8.dp)
                    .background(accent, CircleShape)
            )
        }
    }

}