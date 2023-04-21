package com.andriybobchuk.cocreate.feature.collaborators.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.andriybobchuk.cocreate.R
import com.andriybobchuk.cocreate.core.presentation.components.post.Post
import com.andriybobchuk.cocreate.ui.theme.background_gray100
import com.andriybobchuk.cocreate.ui.theme.poppins
import com.andriybobchuk.cocreate.ui.theme.title_black
import com.andriybobchuk.cocreate.ui.theme.white

@Composable
fun MyPostsScreen(
    navController: NavController
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .height(50.dp)
                .background(white),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Collaborators",
                fontSize = 23.sp, fontWeight = FontWeight.Black, color = title_black, fontFamily = poppins,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 50.dp)
                .verticalScroll(rememberScrollState())
                .background(background_gray100)
        ) {
            // List of people can be added here
            Collaborator(name = "Andrew Berntal", description = "Actor, Atlanta", imageUrl = "https://www.google.com/url?sa=i&url=https%3A%2F%2Fm.imdb.com%2Ftitle%2Ftt1804262%2Fmediaviewer%2Frm2423961088&psig=AOvVaw2VfaVQSn7ChD2j0H1KgJe-&ust=1682176354401000&source=images&cd=vfe&ved=0CBEQjRxqFwoTCMiCs7Khu_4CFQAAAAAdAAAAABAR", {}, {})
            Collaborator(name = "Andrew Berntal", description = "Actor, Atlanta", imageUrl = "https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.looper.com%2F454933%2Fwhy-jon-bernthal-was-never-the-same-after-the-walking-dead%2F&psig=AOvVaw2VfaVQSn7ChD2j0H1KgJe-&ust=1682176354401000&source=images&cd=vfe&ved=0CBEQjRxqFwoTCMiCs7Khu_4CFQAAAAAdAAAAABAJ", {}, {})
            Collaborator(name = "Andrew Berntal", description = "Actor, Atlanta", imageUrl = "https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.looper.com%2F454933%2Fwhy-jon-bernthal-was-never-the-same-after-the-walking-dead%2F&psig=AOvVaw2VfaVQSn7ChD2j0H1KgJe-&ust=1682176354401000&source=images&cd=vfe&ved=0CBEQjRxqFwoTCMiCs7Khu_4CFQAAAAAdAAAAABAJ", {}, {})
            Collaborator(name = "Andrew Berntal", description = "Actor, Atlanta", imageUrl = "https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.looper.com%2F454933%2Fwhy-jon-bernthal-was-never-the-same-after-the-walking-dead%2F&psig=AOvVaw2VfaVQSn7ChD2j0H1KgJe-&ust=1682176354401000&source=images&cd=vfe&ved=0CBEQjRxqFwoTCMiCs7Khu_4CFQAAAAAdAAAAABAJ", {}, {})

        }
    }
}