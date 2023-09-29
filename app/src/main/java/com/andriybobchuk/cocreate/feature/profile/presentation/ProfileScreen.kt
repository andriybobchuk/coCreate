package com.andriybobchuk.cocreate.feature.profile.presentation

import android.view.View
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
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
import com.andriybobchuk.cocreate.core.presentation.components.Avatar
import com.andriybobchuk.cocreate.core.presentation.components.post.Post
import com.andriybobchuk.cocreate.ui.theme.*
import com.andriybobchuk.cocreate.util.generateShortUserDescription
import com.andriybobchuk.navigation.Screens

enum class TabItem(val title: String) {
    TAB_1("My Posts"),
    TAB_2("Saved Posts"),
}

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val profileData = viewModel.state.value
    val posts = viewModel.postsState.value

    val coverHeight = 180.dp
    val avatarSize = 110.dp
    val roundedCornersCorrection = 30.dp // We lift card & icon up by this value
    val shiftIconTopBy = 20.dp // The bigger this value is, the higher icon is from the middle

    var selectedTab by remember { mutableStateOf(TabItem.TAB_1) }

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
//            Button(
//                modifier = Modifier
//                    .padding(16.dp)
//                    .align(Alignment.TopStart)
//                    .width(40.dp)
//                    .height(40.dp),
//                onClick = { /* navigate back */ },
//                shape = CircleShape,
//                colors = ButtonDefaults.buttonColors(backgroundColor = background_gray200),
//                elevation = ButtonDefaults.elevation(10.dp),
//            ) {
//                Icon(
//                    painter = painterResource(id = R.drawable.ic_arrow),
//                    contentDescription = null,
//                    tint = accent,
//                    modifier = Modifier.size(60.dp)
//                )
//            }
//
//            // Profile Detail Button
//            Button(
//                modifier = Modifier
//                    .padding(16.dp)
//                    .align(Alignment.TopEnd)
//                    .width(40.dp)
//                    .height(40.dp),
//                onClick = { /* navigate back */ },
//                shape = CircleShape,
//                colors = ButtonDefaults.buttonColors(backgroundColor = background_gray200),
//                elevation = ButtonDefaults.elevation(10.dp),
//            ) {
//                Icon(
//                    painter = painterResource(id = R.drawable.ic_edit),
//                    contentDescription = null,
//                    tint = accent,
//                    modifier = Modifier.size(10.dp)
//                )
//            }

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
                    if (profileData.city != "" || profileData.position != "") {
                        Text(
                            text = generateShortUserDescription(profileData.position, profileData.city),
                            fontSize = 13.sp,
                            modifier = Modifier.align(CenterHorizontally),
                            fontWeight = FontWeight.Light,
                            color = typo_gray200,
                            fontFamily = poppins
                        )
                    }
                    Spacer(modifier = Modifier.height(0.dp))
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
                                text = "Add Post",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = white,
                                fontFamily = poppins,
                            )
                        }
                        Button(
                            onClick = {
                                navController.navigate(Screens.ProfileEditScreen.route)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1.0F)
                                .padding(4.dp),
                            elevation = ButtonDefaults.elevation(0.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = button_gray150,
                                contentColor = accent // clicked state
                            ),
                            shape = RoundedCornerShape(11.dp)
                        ) {
                            Text(
                                text = "My Info",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = accent,
                                fontFamily = poppins,
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
                Avatar(radius = avatarSize, font = 26.sp, avatarUrl = profileData.avatar, name = profileData.name)
            }
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .clip(RoundedCornerShape(Constants.CARD_ROUNDED_CORNERS))
        ) {
            TabRow(
                selectedTabIndex = selectedTab.ordinal,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp)
                    .background(white),
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        color = accent,
                        height = 2.dp,
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab.ordinal])
                    )
                }
            ) {
                TabItem.values().forEachIndexed { index, tabItem ->
                    Tab(
                        modifier = Modifier.background(white),
                        selected = selectedTab == tabItem,
                        onClick = {
                            selectedTab = tabItem
                            // Handle navigation or content update based on the selected tab
                        },
                        text = {
                            Text(
                                fontFamily = poppins,
                                fontSize = 14.sp,
                                text = tabItem.title,
                                color = if (selectedTab == tabItem) accent else typo_gray100
                            )
                        }
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {

            posts.forEach { post ->
                Post(
                    ownerAvatar = post.postAuthor.avatar,
                    ownerName = post.postAuthor.name,
                    publishedTime = post.postBody.published,
                    title = post.postBody.title,
                    contentText = post.postBody.desc,
                    tags = post.postBody.tags,
                    isMine = true,
                    onSaveClick = {

                    },
                    onCommentClick = {
                        System.out.println("Comment clicked")
                        navController.navigate(
                            "postDetail/{user}"
                                .replace(
                                    oldValue = "{user}",
                                    newValue = post.postBody.uid
                                )
                        )
                    },
                    onReadMoreClick = {
                        navController.navigate(
                            "postDetail/{user}"
                                .replace(
                                    oldValue = "{user}",
                                    newValue = post.postBody.uid
                                )
                        )
                    },
                    onEditClick = {
                        if(post.postBody.isMine) {
                            navController.navigate(
                                "postEdit/{post}"
                                    .replace(
                                        oldValue = "{post}",
                                        newValue = post.postBody.uid
                                    )
                            )
                        }
                    },
                    onProfileClick = {
                        viewModel.navigateToProfileOrDetail(navController, post.postAuthor.uid)
                    }
                )
            }
        }
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










