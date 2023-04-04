package com.andriybobchuk.cocreate.feature.auth.presentation.use_case.register

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.andriybobchuk.cocreate.R
import com.andriybobchuk.cocreate.ui.theme.*
import com.andriybobchuk.navigation.Screens
import kotlinx.coroutines.launch


@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: RegisterViewModel = hiltViewModel()
) {

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val state = viewModel.registerState.collectAsState(initial = null)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 30.dp, end = 30.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Let's get started!",
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            color = title_black,
            fontFamily = poppins
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Enter your credentials to register",
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            color = typo_gray,
            fontFamily = poppins
        )
        Spacer(modifier = Modifier.height(50.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = email,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = input_fields,
                cursorColor = Color.Black,
                disabledLabelColor = input_fields,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            onValueChange = {
                email = it
            },
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            placeholder = {
                Text(text = "Email")
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = password,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = input_fields,
                cursorColor = Color.Black,
                disabledLabelColor = input_fields,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            onValueChange = {
                password = it
            },
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            placeholder = {
                Text(text = "Password")
            }
        )
        Button(
            onClick = {
                scope.launch {
                    viewModel.registerUser(email, password)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = accent,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(15.dp)
        ) {
            Text(
                text = "Register",
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                color = white,
                fontFamily = poppins,
                modifier = Modifier
                    .padding(7.dp)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            if (state.value?.isLoading == true) {
                CircularProgressIndicator()
            }
        }
        Text(
            modifier = Modifier
                .padding(15.dp)
                .clickable {
                    navController.navigate(Screens.LoginScreen.route)
                },
            text = "Already have an account? Log In",
            fontWeight = FontWeight.Bold, color = Color.Black, fontFamily = poppins
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            modifier = Modifier
                .padding(
                    top = 40.dp,
                ),
            text = "Or connect with",
            fontWeight = FontWeight.Normal, color = typo_gray, fontFamily = poppins
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp), horizontalArrangement = Arrangement.Center
        ) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    modifier = Modifier.size(50.dp),
                    painter = painterResource(id = R.drawable.ic_google),
                    contentDescription = "Google Icon", tint = Color.Unspecified
                )
            }
            Spacer(modifier = Modifier.width(30.dp))
            IconButton(onClick = {

            }) {
                Icon(
                    modifier = Modifier.size(52.dp),
                    painter = painterResource(id = R.drawable.ic_facebook),
                    contentDescription = "Google Icon", tint = Color.Unspecified
                )
            }

        }
        LaunchedEffect(key1 = state.value?.isSuccess) {
            scope.launch {
                if (state.value?.isSuccess?.isNotEmpty() == true) {
                    val success = state.value?.isSuccess
                    Toast.makeText(context, "$success", Toast.LENGTH_LONG).show()
                }
            }
        }
        LaunchedEffect(key1 = state.value?.isError) {
            scope.launch {
                if (state.value?.isError?.isNotBlank() == true) {
                    val error = state.value?.isError
                    Toast.makeText(context, "$error", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}


