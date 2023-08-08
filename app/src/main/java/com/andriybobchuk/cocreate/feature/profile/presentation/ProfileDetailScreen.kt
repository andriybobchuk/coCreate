package com.andriybobchuk.cocreate.feature.profile.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.text.TextStyle
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
fun ProfileDetailScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val profileData = viewModel.state.value

    var editedName by remember { mutableStateOf(profileData.name) }
    var editedSurname by remember { mutableStateOf(profileData.position) }
    var editedAboutMe by remember { mutableStateOf(profileData.city) }

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
                onClick = { },
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
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(1.dp))
            Text(
                text = profileData.email,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
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
                    onValueChange = { editedName = it }
                )

                CcInputField(
                    title = "City",
                    value = profileData.city,
                    onValueChange = { editedSurname = it }
                )

                CcInputField(
                    title = "Profile Description",
                    value = "Hi, I'm an Android Dev at Samsung!",
                    onValueChange = { editedAboutMe = it }
                )

                CcInputField(
                    title = "Tags",
                    value = "Figma, Clion, AS, coCreate",
                    onValueChange = { editedAboutMe = it }
                )

                CcInputField(
                    title = "Website",
                    value = profileData.website,
                    onValueChange = { editedAboutMe = it }
                )

                CcInputField(
                    title = "Github",
                    value = profileData.github,
                    onValueChange = { editedAboutMe = it }
                )

                // Add more GrayTextFieldWithLabel for links to GitHub, etc.

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