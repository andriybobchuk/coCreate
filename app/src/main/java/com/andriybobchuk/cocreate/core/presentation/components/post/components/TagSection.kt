package com.andriybobchuk.cocreate.core.presentation.components.post.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andriybobchuk.cocreate.ui.theme.poppins

@Composable
fun TagSection(sectionName: String, tags: List<String>) {
    Text(
        text = sectionName,
        fontSize = 12.sp,
        fontFamily = poppins,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 8.dp)
    )
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        items(tags) { tag ->
            Tag(text = tag, isHighlighted = false)
            Spacer(modifier = Modifier.width(2.dp))
        }
    }
}