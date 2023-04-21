package com.andriybobchuk.cocreate.core.presentation.components.post.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andriybobchuk.cocreate.ui.theme.button_gray150
import com.andriybobchuk.cocreate.ui.theme.poppins
import com.andriybobchuk.cocreate.ui.theme.red
import com.andriybobchuk.cocreate.ui.theme.typo_gray200

@Composable
fun FeedbackButton(
    imageVector: ImageVector,
    count: Int,
    isHighlighted: Boolean = false,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(backgroundColor = button_gray150),
        shape = CircleShape,
        elevation = ButtonDefaults.elevation(0.dp),
        modifier = Modifier
            .height(36.dp)
            .padding(end = 8.dp),
    ) {
        if(isHighlighted) {
            Icon(
                imageVector = imageVector,
                contentDescription = null,
                tint = red,
                modifier = Modifier.size(16.dp)
            )
        } else {
            Icon(
                imageVector = imageVector,
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