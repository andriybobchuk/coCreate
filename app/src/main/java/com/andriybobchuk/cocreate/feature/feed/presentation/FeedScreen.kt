package com.andriybobchuk.cocreate.feature.feed.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.andriybobchuk.cocreate.ui.theme.poppins
import com.andriybobchuk.cocreate.ui.theme.title_black

@Composable
fun FeedScreen(
    navController: NavController
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "feed screen")
    }
}