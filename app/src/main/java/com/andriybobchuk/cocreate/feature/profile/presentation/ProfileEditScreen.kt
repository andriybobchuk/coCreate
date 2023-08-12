package com.andriybobchuk.cocreate.feature.profile.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.andriybobchuk.cocreate.R
import com.andriybobchuk.cocreate.core.presentation.components.input_field.CcInputField
import com.andriybobchuk.cocreate.ui.theme.*
import com.andriybobchuk.navigation.Screens

@Composable
fun ProfileEditScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val profileData = viewModel.state.value

    var editedProfession by remember { mutableStateOf(profileData.position) }
    var editedCity by remember { mutableStateOf(profileData.city) }
    var editedDescription by remember { mutableStateOf(profileData.desc) }
    var editedTags by remember { mutableStateOf(profileData.tags) }
    var editedWebsite by remember { mutableStateOf(profileData.website) }
    var editedGithub by remember { mutableStateOf(profileData.github) }

    // Add more fields as needed for links to GitHub, etc.

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
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
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = {
                    viewModel.logOut()
                    navController.navigate(Screens.LoginScreen.route)
                          },
                modifier = Modifier.padding(end = 10.dp),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_logout),
                    contentDescription = "Log Out",
                    tint = red
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 50.dp)
                .verticalScroll(rememberScrollState())
                .background(white),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Icon
            Box(
                modifier = Modifier
                    .background(Color.White, shape = CircleShape)
                    .shadow(elevation = 10.dp, shape = CircleShape)
            ) {
                // Display the avatar icon which overlaps the cover and the white card
                if (profileData.avatar != "") {
                    Image(
                        painter = rememberAsyncImagePainter(model = profileData.avatar),
                        contentDescription = "Avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                            .align(Alignment.TopCenter)
                    )
                } else {
                    Box(
                        Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                            .background(purple)
                            .align(Alignment.TopCenter),
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
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = profileData.name,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                fontFamily = poppins,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(1.dp))
            Text(
                text = profileData.email,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                fontFamily = poppins,
                color = typo_gray200
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Editable Fields
                CcInputField(
                    title = "Profession",
                    value = profileData.position,
                    isSingleLine = true,
                    onValueChange = { newValue -> editedProfession = newValue }
                )

                CcInputField(
                    title = "City",
                    value = profileData.city,
                    isSingleLine = true,
                    onValueChange = { newValue -> editedCity = newValue }
                )

                CcInputField(
                    title = "Profile Description",
                    value = profileData.desc,
                    isSingleLine = false,
                    onValueChange = { newValue -> editedDescription = newValue }
                )

                CcInputField(
                    title = "Tags",
                    value = profileData.tags.joinToString(", "),
                    isSingleLine = false,
                    onValueChange = { newValue ->
                        newValue.replace(",", ", ")
                        editedTags = newValue.split(", |,").toList()
                    }
                )

                CcInputField(
                    title = "Website",
                    value = profileData.website,
                    isSingleLine = true,
                    onValueChange = { newValue -> editedWebsite = newValue }
                )

                CcInputField(
                    title = "Github",
                    value = profileData.github,
                    isSingleLine = true,
                    onValueChange = { newValue -> editedGithub = newValue }
                )

                Button(
                    onClick = {
                        // Save editedName, editedSurname, editedAboutMe, and other fields
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(top = 20.dp),
                    elevation = ButtonDefaults.elevation(5.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = accent,
                        contentColor = white // clicked state
                    ),
                    shape = RoundedCornerShape(11.dp)
                ) {
                    Text(
                        text = "Save Changes",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = white,
                        fontFamily = poppins,
                    )
                }
            }
        }
    }
}