package com.andriybobchuk.cocreate.core.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.andriybobchuk.cocreate.ui.theme.*
import kotlin.random.Random

@Composable
fun Avatar(
    radius: Dp,
    font: TextUnit,
    avatarUrl: String,
    name: String
) {
    if (avatarUrl.isNotBlank()) {
        Image(
            painter = rememberAsyncImagePainter(model = avatarUrl),
            contentDescription = "Avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(radius)
                .clip(CircleShape)
        )
    } else {
        Box(
            modifier = Modifier
                .size(radius)
                .clip(CircleShape)
                .background(purple)
        ) {
            Text(
                text = name.take(1).toUpperCase(),
                fontSize = font,
                color = Color.White,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

//fun getRandomColor(): Color {
//    val colors = listOf(orange, red, green, accent, purple) // Add more colors as needed
//    val randomIndex = Random.nextInt(colors.size)
//    return colors[randomIndex]
//}
