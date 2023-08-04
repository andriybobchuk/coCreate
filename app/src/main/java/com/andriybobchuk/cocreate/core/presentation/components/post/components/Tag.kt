package com.andriybobchuk.cocreate.core.presentation.components.post.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andriybobchuk.cocreate.ui.theme.*

@Composable
fun Tag(text: String, isHighlighted: Boolean = false) {
    if(isHighlighted) {
        Text(
            text = text,
            fontSize = 13.sp,
            fontFamily = poppins,
            color = accent,
            modifier = Modifier
                .background(white, RoundedCornerShape(7.dp))
                .padding(horizontal = 6.dp, vertical = 2.dp)
        )
    } else {
        Text(
            text = text,
            fontSize = 13.sp,
            fontFamily = poppins,
            color = typo_gray200,
            modifier = Modifier
                .background(background_gray100, RoundedCornerShape(7.dp))
                .padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}