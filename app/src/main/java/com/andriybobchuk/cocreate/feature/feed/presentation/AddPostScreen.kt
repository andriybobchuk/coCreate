package com.andriybobchuk.cocreate.feature.feed.presentation

import com.andriybobchuk.cocreate.feature.profile.presentation.ProfileViewModel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.andriybobchuk.cocreate.R
import com.andriybobchuk.cocreate.core.domain.model.Post
import com.andriybobchuk.cocreate.core.presentation.components.input_field.CcInputField
import com.andriybobchuk.cocreate.ui.theme.*

@Composable
fun AddPostScreen(
    navController: NavController,
    viewModel: AddPostViewModel = hiltViewModel()
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var tags by remember { mutableStateOf("") }

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
            Text(
                text = "Add an Idea \uD83D\uDCA1",
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                color = title_black,
                fontFamily = poppins,
                modifier = Modifier
                    .padding(start = 16.dp)
                //.weight(1f)
            )
            Spacer(modifier = Modifier.weight(1f))
//            IconButton(
//                onClick = { navController.popBackStack() },
//                modifier = Modifier.padding(end = 10.dp),
//            ) {
//                Icon(
//                    painter = painterResource(id = R.drawable.ic_delete),
//                    contentDescription = "Cancel",
//                    tint = red
//                )
//            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = (50 + 5).dp)
                .verticalScroll(rememberScrollState())
                .background(white),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Editable Fields
                CcInputField(
                    title = "Proposal Title",
                    value = title,
                    isSingleLine = true,
                    onValueChange = { newValue -> title = newValue }
                )

                CcInputField(
                    title = "Description",
                    value = description,
                    isSingleLine = false,
                    onValueChange = { newValue -> description = newValue }
                )

                CcInputField(
                    title = "Tags",
                    value = tags,
                    isSingleLine = false,
                    onValueChange = { newValue -> tags = newValue }
                )

                Button(
                    onClick = {
                        viewModel.addPost(
                            title = title,
                            desc = description,
                            tags = tags.split(Regex(",\\s*")).map { it.trim() }
                        )
                        navController.popBackStack()
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
                        text = "Publish Post",
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
