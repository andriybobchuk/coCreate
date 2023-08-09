package com.andriybobchuk.cocreate.core.presentation.screens

import android.view.View
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.andriybobchuk.cocreate.R
import com.andriybobchuk.cocreate.core.Constants
import com.andriybobchuk.cocreate.core.presentation.components.post.Post
import com.andriybobchuk.cocreate.ui.theme.*
import com.andriybobchuk.navigation.Screens


@Composable
fun SomeonesProfileScreen(
    navController: NavController,
    viewModel: SomeonesProfileViewModel = hiltViewModel(),
    //sourceViewModel:
) {
    //viewModel.userId = userId
    val profileData = viewModel.state.value

    val coverHeight = 180.dp
    val avatarSize = 110.dp
    val roundedCornersCorrection = 30.dp // We lift card & icon up by this value
    val shiftIconTopBy = 20.dp // The bigger this value is, the higher icon is from the middle

    Column(
        Modifier
            .fillMaxSize()
            .padding(0.dp, 0.dp)
            .verticalScroll(rememberScrollState())
            .background(background_gray100),
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Display the cover image
            Image(
                painter = rememberAsyncImagePainter(model = profileData.avatar),
                contentDescription = "Cover Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(coverHeight)
            )

            // Back Button
            Button(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.TopStart)
                    .width(40.dp)
                    .height(40.dp),
                onClick = { navController.popBackStack() },
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(backgroundColor = white),
                elevation = ButtonDefaults.elevation(10.dp),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow),
                    contentDescription = null,
                    tint = accent,
                    modifier = Modifier.size(60.dp)
                )
            }

            // Display the white card with rounded corners
            Card(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .padding(top = coverHeight - roundedCornersCorrection, bottom = 4.dp)
                    .clip(RoundedCornerShape(Constants.CARD_ROUNDED_CORNERS))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = avatarSize / 2),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = profileData.name,
                        fontSize = 18.sp,
                        modifier = Modifier.align(CenterHorizontally),
                        fontWeight = FontWeight.Black, color = Color.Black, fontFamily = poppins
                    )
                    if(profileData.city != "" && profileData.position != "") {
                        Text(
                            text = profileData.position + ", " + profileData.city,
                            fontSize = 13.sp,
                            modifier = Modifier.align(CenterHorizontally),
                            fontWeight = FontWeight.Light, color = typo_gray200, fontFamily = poppins
                        )
                    }
                    Spacer(modifier = Modifier.height(0.dp))
                    
                    // Row with buttons
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp, horizontal = 5.dp)
                    ) {
                        Button(
                            onClick = { navController.navigate(Screens.AddPostScreen.route) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1.0F)
                                .padding(4.dp),
                            elevation = ButtonDefaults.elevation(5.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = accent,
                                contentColor = white // clicked state
                            ),
                            shape = RoundedCornerShape(11.dp)
                        ) {
                            Text(
                                text = "Message",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = white,
                                fontFamily = poppins,
                            )
                        }
                        Button(
                            onClick = {
                                navController.navigate(Screens.ProfileDetailScreen.route)
                            },
                            modifier = Modifier
                                .wrapContentWidth()
                                .padding(4.dp),
                            elevation = ButtonDefaults.elevation(0.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = button_gray150,
                                contentColor = accent // clicked state
                            ),
                            shape = RoundedCornerShape(11.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_info),
                                contentDescription = null,
                                tint = accent,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Button(
                            onClick = {
                                navController.navigate(Screens.ProfileDetailScreen.route)
                            },
                            modifier = Modifier
                                .wrapContentWidth()
                                .padding(2.dp),
                            elevation = ButtonDefaults.elevation(0.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = button_gray150,
                                contentColor = accent // clicked state
                            ),
                            shape = RoundedCornerShape(11.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_add_user),
                                contentDescription = null,
                                tint = accent,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .align(TopCenter)
                    .padding(top = coverHeight - roundedCornersCorrection - avatarSize / 2 - shiftIconTopBy)
                    .background(Color.White, shape = CircleShape)
                    .padding(0.dp)
                    .shadow(elevation = 10.dp, shape = CircleShape)
            ) {
                // Display the avatar icon which overlaps the cover and the white card
                if (profileData.avatar != "") {
                    Image(
                        painter = rememberAsyncImagePainter(model = profileData.avatar),
                        contentDescription = "Avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(avatarSize)
                            .clip(CircleShape)
                            .align(Alignment.TopCenter)
                    )
                } else {
                    Box(
                        Modifier
                            .size(avatarSize)
                            .clip(CircleShape)
                            .background(purple)
                            .align(TopCenter),
                    ) {
                        Text(
                            text = profileData.name.take(1).toUpperCase(),
                            fontSize = 26.sp,
                            color = Color.White,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
        }

        Post(
            navController = navController,
            ownerAvatar = rememberAsyncImagePainter(model = "https://andriybobchuk.com/images/about.jpg"),
            ownerName = "John Kekho",
            publishedTime = "5 decades ago",
            contentText = "Hello ladies and gentlemen, this is my very first post on coCreate!\n\nHello ladies and gentlemen, this is my very first post on coCreate!Hello ladies and gentlemen, this is my very first post on coCreate!Hello ladies and gentlemen, this is my very first post on coCreate!",
            tags = listOf("Android Studio", "Figma", "Notion", "Clion", "Visual Studio", "MatLab"),
            onLikeClick = { /*TODO*/ },
            onCommentClick = { /*TODO*/ }
        )
        Post(
            navController = navController,
            ownerAvatar = rememberAsyncImagePainter(model = "https://andriybobchuk.com/images/about.jpg"),
            ownerName = "John Kekho",
            publishedTime = "5 decades ago",
            contentText = "Hello ladies and gentlemen, this is my very first post on coCreate!",
            tags = listOf("Android Studio", "Figma", "Notion", "Clion", "Visual Studio", "MatLab"),
            onLikeClick = { /*TODO*/ },
            onCommentClick = { /*TODO*/ }
        )
        Post(
            navController = navController,
            ownerAvatar = rememberAsyncImagePainter(model = "https://andriybobchuk.com/images/about.jpg"),
            ownerName = "John Kekho",
            publishedTime = "5 decades ago",
            contentText = "Hello ladies and gentlemen, this is my very first post on coCreate!\nHello ladies and gentlemen, this is my very first post on coCreate!Hello ladies and gentlemen, this is my very first post on coCreate!",
            tags = listOf("Android Studio", "Figma", "Notion", "Clion", "Visual Studio", "MatLab"),
            onLikeClick = { /*TODO*/ },
            onCommentClick = { /*TODO*/ }
        )

//        Card(
//            modifier = Modifier
//                .padding(vertical = 10.dp)
//                .fillMaxWidth()
//                .clip(RoundedCornerShape(25.dp)),
//        ) {
//            Column(
//                modifier = Modifier.padding(top = 18.dp, bottom = 10.dp, start = 20.dp, end = 20.dp)
//            ) {
//                Text(
//                    text = "Contact",
//                    fontSize = 16.sp,
//                    fontWeight = FontWeight.Bold,
//                    modifier = Modifier.padding(bottom = 16.dp)
//                )
//                SocialMediaLink(
//                    name = "Twitter",
//                    link = "https://twitter.com/yourusername"
//                )
//                SocialMediaLink(
//                    name = "Instagram",
//                    link = "https://instagram.com/yourusername"
//                )
//                SocialMediaLink(
//                    name = "LinkedIn",
//                    link = "https://linkedin.com/in/yourusername"
//                )
//            }
//        }



    }
}

@Composable
fun SocialMediaLink(name: String, link: String) {
    Divider(
        color = MaterialTheme.colors.onSurface.copy(alpha = 0.05f),
        thickness = 0.4.dp,
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(vertical = 4.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.ic_link),
            contentDescription = null,
            modifier = Modifier
                .size(41.dp)
                .padding(end = 15.dp)
        )
        Text(
            text = name,
            fontSize = 15.sp,
            fontWeight = FontWeight.Normal,
        )
    }
}










