package com.andriybobchuk.cocreate.core.presentation.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
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
import com.andriybobchuk.cocreate.ui.theme.*
import com.andriybobchuk.navigation.Screens
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SomeonesProfileScreen(
    navController: NavController,
    viewModel: SomeonesProfileViewModel = hiltViewModel(),
    id: String
) {
    viewModel.getProfileDataById(id)
    val profileData = viewModel.state.value

    val coverHeight = 180.dp
    val avatarSize = 110.dp
    val roundedCornersCorrection = 30.dp // We lift card & icon up by this value
    val shiftIconTopBy = 20.dp // The bigger this value is, the higher icon is from the middle

    ///

    val coroutineScope = rememberCoroutineScope()
    val modalSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded },
        skipHalfExpanded = true
    )

    ModalBottomSheetLayout(
        sheetState = modalSheetState,
        sheetShape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp), // Rounded corners at the top
        sheetContent = {
            Column(
                modifier = Modifier
                    //.padding(16.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 16.dp,
                            top = 10.dp,
                            end = 0.dp,
                            bottom = 0.dp
                        ), // Add horizontal padding for spacing
                    horizontalArrangement = Arrangement.SpaceBetween, // Spread items apart horizontally
                ) {
                    Text(
                        text = "Information",
                        fontSize = 18.sp,
                        fontFamily = poppins,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(end = 0.dp) // Add some spacing to the right of the title
                    )
                    Icon(
                        modifier = Modifier.clickable{
                            coroutineScope.launch { modalSheetState.hide() }
                        }
                            .size(36.dp)
                            .padding(end = 16.dp),
                        painter = painterResource(id = R.drawable.ic_close),
                        contentDescription = null,
                        tint = typo_gray100,
                    )
                }

                Divider(
                    modifier = Modifier.padding(
                        start = 16.dp,
                        top = 5.dp,
                        end = 16.dp,
                        bottom = 5.dp),
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.05f),
                    thickness = 0.4.dp
                )

                // Email Row
                UserInfoItem(icon = painterResource(id = R.drawable.ic_email), text = "Email: " + profileData.email)

                Divider(
                    modifier = Modifier.padding(
                        start = 16.dp,
                        top = 5.dp,
                        end = 16.dp,
                        bottom = 5.dp),
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.05f),
                    thickness = 0.4.dp
                )

                // City and Profession Rows
                UserInfoItem(icon = painterResource(id = R.drawable.ic_home), text ="City: " + profileData.city)
                UserInfoItem(icon = painterResource(id = R.drawable.ic_profile1), text = "Specialization: " + profileData.position)
                // Description Row
                UserInfoItem(
                    icon = painterResource(id = R.drawable.ic_book),
                    text = profileData.desc
                )

                Divider(
                    modifier = Modifier.padding(
                        start = 16.dp,
                        top = 5.dp,
                        end = 16.dp,
                        bottom = 5.dp),
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.05f),
                    thickness = 0.4.dp
                )

                // Website and GitHub Rows
                UserInfoLink(icon = painterResource(id = R.drawable.ic_link), text = "Personal Website", url = profileData.website)
                UserInfoLink(icon = painterResource(id = R.drawable.ic_link), text = "GitHub Profile", url = profileData.github)

            }
        }
    ) {
        Scaffold {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
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
                                .padding(
                                    top = coverHeight - roundedCornersCorrection,
                                    bottom = 4.dp
                                )
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
                                    fontWeight = FontWeight.Black,
                                    color = Color.Black,
                                    fontFamily = poppins
                                )
                                if (profileData.city != "" || profileData.position != "") {
                                    Text(
                                        text = profileData.position + " " + profileData.city,
                                        fontSize = 13.sp,
                                        modifier = Modifier.align(CenterHorizontally),
                                        fontWeight = FontWeight.Light,
                                        color = typo_gray200,
                                        fontFamily = poppins
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
                                            coroutineScope.launch {
                                                if (modalSheetState.isVisible)
                                                    modalSheetState.hide()
                                                else
                                                    modalSheetState.animateTo(ModalBottomSheetValue.Expanded)
                                            }
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
                }
            }
        }
    }
}
@Composable
fun UserInfoItem(icon: Painter, text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = icon,
            contentDescription = "icon",
            tint = typo_gray200,
            modifier = Modifier
                .size(28.dp)
                .padding(end = 11.dp),

        )
        Text(
            text = text,
            fontSize = 13.sp,
            color = typo_gray100,
            fontFamily = poppins,
        )
    }
}

@Composable
fun UserInfoLink(icon: Painter, text: String, url: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Open URL */ }
            .padding(vertical = 6.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = icon,
            contentDescription = "Link",
            tint = accent,
            modifier = Modifier
                .size(36.dp)
                .padding(end = 11.dp)
        )
        Text(
            text = text,
            color = title_black,
            fontSize = 14.sp,
            fontFamily = poppins,
            modifier = Modifier.clickable { /* Open URL */ }
        )
    }
}