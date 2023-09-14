package com.andriybobchuk.cocreate.core.presentation.components.post.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andriybobchuk.cocreate.R
import com.andriybobchuk.cocreate.ui.theme.*

@Composable
fun FeedbackButton(
    painter: Painter,
    count: Int,
    isHighlighted: Boolean = false,
    onClick: () -> Unit,
) {
    val isLikedState = rememberUpdatedState(isHighlighted)

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(backgroundColor = background_gray100),
        shape = CircleShape,
        elevation = ButtonDefaults.elevation(0.dp),
        modifier = Modifier
            .height(36.dp)
            .padding(end = 8.dp),
    ) {
        if(isLikedState.value) {
            Icon(
                painter = painter,
                contentDescription = null,
                tint = red,
                modifier = Modifier.size(16.dp)
            )
        } else {
            Icon(
                painter = painter,
                contentDescription = null,
                tint = typo_gray200,
                modifier = Modifier.size(16.dp)
            )
        }
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = count.toString(),
            color = typo_gray200,
            fontSize = 12.sp,
            fontFamily = poppins,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}