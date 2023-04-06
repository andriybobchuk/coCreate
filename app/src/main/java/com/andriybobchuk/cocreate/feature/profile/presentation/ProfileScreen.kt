package com.andriybobchuk.cocreate.feature.profile.presentation

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.andriybobchuk.cocreate.R
import com.andriybobchuk.cocreate.ui.theme.*
import com.andriybobchuk.navigation.Screens
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    navController: NavController
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp, 0.dp)
            .verticalScroll(rememberScrollState()),
    ) {
//        IconButton(onClick = { navController.navigate(Screens.RegisterScreen.route) }) {
//            Icon(Icons.Default.ArrowBack, contentDescription = "Back button")
//        }
        Spacer(modifier = Modifier.height(30.dp))

        if (null != null) {
            Image(
                painter = painterResource(id = R.drawable.ic_profile),
                contentDescription = "User Profile Image",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape)
                    .shadow(
                        elevation = 4.dp,
                        shape = CircleShape,
                        clip = true
                    )
                    .align(CenterHorizontally),
            )
        } else {
            Box(
                Modifier
                    .size(70.dp)
                    .clip(CircleShape)
                    .background(purple)
//                    .shadow(
//                        elevation = 4.dp,
//                        //shape = CircleShape,
//                        //clip = true
//                    )
                    .align(CenterHorizontally),
            ) {
                Text(
                    text = "Andriy".take(1).toUpperCase(),
                    fontSize = 19.sp,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Andriy Bobchuk",
            fontSize = 18.sp,
            modifier = Modifier.align(CenterHorizontally),
            fontWeight = FontWeight.Black, color = Color.Black, fontFamily = poppins
        )
        Text(
            text = "Mobile Developer, California",
            fontSize = 14.sp,
            modifier = Modifier.align(CenterHorizontally),
            fontWeight = FontWeight.Normal, color = typo_gray, fontFamily = poppins
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
//                scope.launch {
//                    viewModel.loginUser(email, password)
//                }
            },
            modifier = Modifier
                .wrapContentWidth()
                .padding(20.dp, 5.dp, 20.dp, 5.dp)
                .align(CenterHorizontally),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Black,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(15.dp)
        ) {
            Text(
                text = "Edit Info",
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                color = white,
                fontFamily = poppins,
                modifier = Modifier
                    .padding(20.dp, 5.dp, 20.dp, 5.dp)
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Social network cards
//        LazyColumn(modifier = Modifier.weight(1f)) {
//            item {
//                SocialCard(
//                    networkName = "GitHub",
//                    networkIcon = Icons.Default.Person,
//                    networkLink = "https://github.com/andriybobchuk/"
//                )
//            }
//            item {
//                SocialCard(
//                    networkName = "GitHub",
//                    networkIcon = Icons.Default.Person,
//                    networkLink = "https://github.com/andriybobchuk/"
//                )
//            }
//            item {
//                SocialCard(
//                    networkName = "GitHub",
//                    networkIcon = Icons.Default.Person,
//                    networkLink = "https://github.com/andriybobchuk/"
//                )
//            }
//            item {
//                SocialCard(
//                    networkName = "GitHub",
//                    networkIcon = Icons.Default.Person,
//                    networkLink = "https://github.com/andriybobchuk/"
//                )
//            }
//        }
        SocialCard(
            networkName = "GitHub",
            networkIcon = Icons.Default.Person,
            networkLink = "https://github.com/andriybobchuk/"
        )
        SocialCard(
            networkName = "GitHub",
            networkIcon = Icons.Default.Person,
            networkLink = "https://github.com/andriybobchuk/"
        )
        SocialCard(
            networkName = "GitHub",
            networkIcon = Icons.Default.Person,
            networkLink = "https://github.com/andriybobchuk/"
        )
        SocialCard(
            networkName = "GitHub",
            networkIcon = Icons.Default.Person,
            networkLink = "https://github.com/andriybobchuk/"
        )
        SocialCard(
            networkName = "GitHub",
            networkIcon = Icons.Default.Person,
            networkLink = "https://github.com/andriybobchuk/"
        )
    }
}

@Composable
fun SocialCard(
    networkName: String,
    networkIcon: ImageVector,
    networkLink: String
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = 0.dp,
        backgroundColor = background,
        modifier = Modifier
            .padding(vertical = 5.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
            .clickable { /* Handle card click */ }
            .padding(24.dp)
        ) {
            Row() {
                Icon(
                    imageVector = networkIcon,
                    contentDescription = networkName,
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = networkName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black, color = Color.Black, fontFamily = poppins,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .align(Alignment.CenterVertically)
                )
            }
            Text(
                text = networkLink,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal, color = accent, fontFamily = poppins,
                modifier = Modifier
                    .padding(start = 8.dp)
            )
        }
    }
}