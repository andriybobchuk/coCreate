package com.andriybobchuk.cocreate.feature.feed.presentation

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.andriybobchuk.cocreate.R
import com.andriybobchuk.cocreate.core.presentation.components.Comment
import com.andriybobchuk.cocreate.core.presentation.components.post.Post
import com.andriybobchuk.cocreate.core.presentation.components.post.components.FeedbackButton
import com.andriybobchuk.cocreate.core.presentation.components.post.components.TagSection
import com.andriybobchuk.cocreate.feature.profile.presentation.ProfileViewModel
import com.andriybobchuk.cocreate.ui.theme.*
import com.andriybobchuk.navigation.Screens

@Composable
fun PostDetailScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier.background(background_gray100).fillMaxSize().verticalScroll(rememberScrollState())
    ) {
        // Header
        Row(
            modifier = Modifier
                .height(50.dp)
                .background(white),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.padding(start = 10.dp),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow),
                    contentDescription = "Back"
                )
            }
            Text(
                text = "Details  \uD83D\uDCDC",
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                color = title_black,
                fontFamily = poppins,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1f)
            )
        }
        Box(Modifier.background(white)) {
            // Post
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(start = 16.dp, end = 16.dp, top = (0 + 5).dp, bottom = 6.dp)
                    .background(white),
                //horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    Modifier.background(white)
                    //verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = rememberAsyncImagePainter("https://www.google.com/url?sa=i&url=https%3A%2F%2Fencrypted-tbn1.gstatic.com%2Flicensed-image%3Fq%3Dtbn%3AANd9GcRmyqPeGNmHcItdNfp1tzxjPCQEUqjy8-HGMNCdn4IsZ30dwaWvwfHs8bZe8hqD940deEx6sx1rboYtNTY&psig=AOvVaw10xSDiBzac0hD6NDxVcDY1&ust=1691160273305000&source=images&cd=vfe&opi=89978449&ved=0CBEQjRxqFwoTCLj1247dwIADFQAAAAAdAAAAABAE"),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(41.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(11.dp))
                    Column(
                        modifier = Modifier
                            .wrapContentHeight()
                            .weight(1f)
                    ) {
                        Text(
                            text = "Youssef Al Bali",
                            fontSize = 14.sp,
                            fontFamily = poppins,
                        )
                        Text(
                            text = "21.09.2002",
                            fontSize = 12.sp,
                            fontFamily = poppins,
                            color = typo_gray100,
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "AI based recommender system",
                    fontSize = 14.sp,
                    fontFamily = poppins,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = "Hello ladies and gentlemen, this is my very first post on coCreate!\nHello ladies and gentlemen, this is my very first post on coCreate!Hello ladies and gentlemen, this is my very first post on coCreate!",
                    fontSize = 14.sp,
                    fontFamily = poppins,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                TagSection(
                    sectionName = "Tags",
                    tags = listOf(
                        "Android Studio",
                        "Figma",
                        "Notion",
                        "Clion",
                        "Visual Studio",
                        "MatLab"
                    ),
                )
                Divider(
                    modifier = Modifier.padding(top = 10.dp),
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.05f),
                    thickness = 0.4.dp
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .background(white)
                ) {
                    FeedbackButton(
                        painter = painterResource(id = R.drawable.ic_like),
                        isHighlighted = true,
                        count = 12,
                        onClick = {})
//                FeedbackButton(
//                    painter = painterResource(id = R.drawable.ic_messages),
//                    count = 4,
//                    onClick = {
//                        navController.navigate(
//                            Screens.PostDetailScreen.route
//                        )
//                    })

                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(background_gray100)
        ) {
            Comment(
                imageUrl = "https://www.google.com/url?sa=i&url=https%3A%2F%2Fencrypted-tbn1.gstatic.com%2Flicensed-image%3Fq%3Dtbn%3AANd9GcRmyqPeGNmHcItdNfp1tzxjPCQEUqjy8-HGMNCdn4IsZ30dwaWvwfHs8bZe8hqD940deEx6sx1rboYtNTY&psig=AOvVaw10xSDiBzac0hD6NDxVcDY1&ust=1691160273305000&source=images&cd=vfe&opi=89978449&ved=0CBEQjRxqFwoTCLj1247dwIADFQAAAAAdAAAAABAE",
                username = "Alice Jackson",
                timestamp = "3 hours ago",
                commentText = "Great post! Keep up the good work.",
                onProfileClick = {}
            )
            Comment(
                imageUrl = "https://www.google.com/url?sa=i&url=https%3A%2F%2Fencrypted-tbn1.gstatic.com%2Flicensed-image%3Fq%3Dtbn%3AANd9GcRmyqPeGNmHcItdNfp1tzxjPCQEUqjy8-HGMNCdn4IsZ30dwaWvwfHs8bZe8hqD940deEx6sx1rboYtNTY&psig=AOvVaw10xSDiBzac0hD6NDxVcDY1&ust=1691160273305000&source=images&cd=vfe&opi=89978449&ved=0CBEQjRxqFwoTCLj1247dwIADFQAAAAAdAAAAABAE",
                username = "Bob Marley",
                timestamp = "2 hours ago",
                commentText = "I found this really helpful. Thanks for sharing!",
                onProfileClick = {}
            )
            Comment(
                imageUrl = "https://www.google.com/url?sa=i&url=https%3A%2F%2Fencrypted-tbn1.gstatic.com%2Flicensed-image%3Fq%3Dtbn%3AANd9GcRmyqPeGNmHcItdNfp1tzxjPCQEUqjy8-HGMNCdn4IsZ30dwaWvwfHs8bZe8hqD940deEx6sx1rboYtNTY&psig=AOvVaw10xSDiBzac0hD6NDxVcDY1&ust=1691160273305000&source=images&cd=vfe&opi=89978449&ved=0CBEQjRxqFwoTCLj1247dwIADFQAAAAAdAAAAABAE",
                username = "Charlie Puth",
                timestamp = "1 hour ago",
                commentText = "Amazing content. Looking forward to more posts like this. I liked specifically the idea about incorporationg or, so to speak injectiong the AI into book recommender system, we could work on it together if you stil have some free time left. Waiting for a collab request and a DM!",
                onProfileClick = {}
            )
            Comment(
                imageUrl = "https://www.google.com/url?sa=i&url=https%3A%2F%2Fencrypted-tbn1.gstatic.com%2Flicensed-image%3Fq%3Dtbn%3AANd9GcRmyqPeGNmHcItdNfp1tzxjPCQEUqjy8-HGMNCdn4IsZ30dwaWvwfHs8bZe8hqD940deEx6sx1rboYtNTY&psig=AOvVaw10xSDiBzac0hD6NDxVcDY1&ust=1691160273305000&source=images&cd=vfe&opi=89978449&ved=0CBEQjRxqFwoTCLj1247dwIADFQAAAAAdAAAAABAE",
                username = "Tom The Cat",
                timestamp = "1 min ago",
                commentText = "Amazing idea brother! Keep up the good work.",
                onProfileClick = {}
            )
            Comment(
                imageUrl = "https://www.google.com/url?sa=i&url=https%3A%2F%2Fencrypted-tbn1.gstatic.com%2Flicensed-image%3Fq%3Dtbn%3AANd9GcRmyqPeGNmHcItdNfp1tzxjPCQEUqjy8-HGMNCdn4IsZ30dwaWvwfHs8bZe8hqD940deEx6sx1rboYtNTY&psig=AOvVaw10xSDiBzac0hD6NDxVcDY1&ust=1691160273305000&source=images&cd=vfe&opi=89978449&ved=0CBEQjRxqFwoTCLj1247dwIADFQAAAAAdAAAAABAE",
                username = "Viktor Frankl",
                timestamp = "12:46",
                commentText = "I found this really helpful. Thanks for sharing!",
                onProfileClick = {}
            )
            Comment(
                imageUrl = "https://www.google.com/url?sa=i&url=https%3A%2F%2Fencrypted-tbn1.gstatic.com%2Flicensed-image%3Fq%3Dtbn%3AANd9GcRmyqPeGNmHcItdNfp1tzxjPCQEUqjy8-HGMNCdn4IsZ30dwaWvwfHs8bZe8hqD940deEx6sx1rboYtNTY&psig=AOvVaw10xSDiBzac0hD6NDxVcDY1&ust=1691160273305000&source=images&cd=vfe&opi=89978449&ved=0CBEQjRxqFwoTCLj1247dwIADFQAAAAAdAAAAABAE",
                username = "Bob Marley",
                timestamp = "09:46",
                commentText = "I found this really helpful. Thanks for sharing!",
                onProfileClick = {}
            )
            Comment(
                imageUrl = "https://www.google.com/url?sa=i&url=https%3A%2F%2Fencrypted-tbn1.gstatic.com%2Flicensed-image%3Fq%3Dtbn%3AANd9GcRmyqPeGNmHcItdNfp1tzxjPCQEUqjy8-HGMNCdn4IsZ30dwaWvwfHs8bZe8hqD940deEx6sx1rboYtNTY&psig=AOvVaw10xSDiBzac0hD6NDxVcDY1&ust=1691160273305000&source=images&cd=vfe&opi=89978449&ved=0CBEQjRxqFwoTCLj1247dwIADFQAAAAAdAAAAABAE",
                username = "Charlie Puth",
                timestamp = "17:01",
                commentText = "Amazing content. Looking forward to more posts like this. I liked specifically the idea about incorporationg or, so to speak injectiong the AI into book recommender system, we could work on it together if you stil have some free time left. Waiting for a collab request and a DM!",
                onProfileClick = {}
            )
        }
    }
}
