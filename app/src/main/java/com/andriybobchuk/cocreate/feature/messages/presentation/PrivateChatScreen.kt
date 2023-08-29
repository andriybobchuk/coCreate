package com.andriybobchuk.cocreate.feature.messages.presentation

import androidx.compose.foundation.Image
import com.andriybobchuk.cocreate.R
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.andriybobchuk.cocreate.feature.messages.domain.model.Message
import com.andriybobchuk.cocreate.ui.theme.*
import com.andriybobchuk.cocreate.util.ccLog

@Composable
fun PrivateChatScreen(
    navController: NavController,
    viewModel: PrivateChatViewModel = hiltViewModel(),
    chatId: String
) {
    ccLog.d("PrivateChatScreen", "ChatId = ${chatId}")
    viewModel.getPrivateChatDataByChatId(chatId)
    val chatData = viewModel.state.value

//    viewModel.getCommentsByPostId(id)
//    val comments = viewModel.commentsState.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(white)
    ) {
        // Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .background(white)
                .clickable { }
                .padding(top = 4.dp)
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow),
                    contentDescription = "Back",
                    tint = title_black
                )
            }
            Spacer(modifier = Modifier.width(5.dp))
            Column(modifier = Modifier.wrapContentHeight()) {
                if (chatData.recipientData.avatar != "") {
                    Image(
                        painter = rememberAsyncImagePainter(model = chatData.recipientData.avatar),
                        contentDescription = "Avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                    )
                } else {
                    Box(
                        Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(purple),
                    ) {
                        Text(
                            text = chatData.recipientData.name.take(1).toUpperCase(),
                            fontSize = 16.sp,
                            color = Color.White,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = chatData.recipientData.name,
                    fontSize = 14.sp,
                    fontFamily = poppins,
                    fontWeight = FontWeight.Bold,
                    color = title_black
                )
                Text(
                    text = chatData.recipientData.position,
                    fontSize = 12.sp,
                    fontFamily = poppins,
                    color = typo_gray100
                )
            }
        }
        Divider(
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.05f),
            thickness = 0.4.dp
        )


        // Messages
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(top = 16.dp)
            ) {
                items(chatData.messages) { message ->
                    MessageItem(
                        message = Message(
                            id = "",
                            content = message.content,
                            senderId = "",
                            time = message.time,
                            isMyMessage = message.isMyMessage,
                        )
                    )
                }
            }

            // State for storing the message text
            val messageTextState = remember { mutableStateOf("") }
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    value = messageTextState.value,
                    onValueChange = { newText -> messageTextState.value = newText },
                    textStyle = TextStyle(color = title_black, fontFamily = poppins),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                        .border(0.5.dp, background_gray100, RoundedCornerShape(16.dp))
                        .padding(8.dp)
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_send),
                    contentDescription = "Send",
                    tint = title_black,
                    modifier = Modifier.clickable {
                        val messageText = messageTextState.value
                        if (messageText.isNotBlank()) {
                            viewModel.send(
                                convoId = chatId,
                                content = messageTextState.value )
                            messageTextState.value = ""
                        }
                    }
                )
            }
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .background(white)
//                    .padding(16.dp),
//                verticalArrangement = Arrangement.Center
//            ) {
//                BasicTextField(
//                    value = messageTextState.value,
//                    onValueChange = { newText -> messageTextState.value = newText },
//                    textStyle = TextStyle(color = title_black),
//                    modifier = Modifier
//                        .weight(1f)
//                        .padding(end = 8.dp)
//                        .border(1.dp, typo_gray100, RoundedCornerShape(16.dp))
//                        .padding(8.dp)
//                )
//                Icon(
//                    painter = painterResource(id = R.drawable.ic_send),
//                    contentDescription = "Send",
//                    tint = accent,
//                    modifier = Modifier.clickable {
//                        // Replace this with your send logic using ViewModel
//                        viewModel.send(
//                                convoId = chatId,
//                                content = messageTextState.value )
//                    }
//                )
//            }
        }
    }
}

@Composable
fun MessageItem(message: Message) {
    val isMyMessage = message.isMyMessage // Determine whether the message is mine

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        if (!isMyMessage) {
            // Other person's profile picture
            Image(
                painter = painterResource(id = R.drawable.ic_profile1),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
            )
        }

        // Message bubble
        Column(
            modifier = Modifier
                .padding(start = 8.dp, end = (if (isMyMessage) 0.dp else 8.dp))
                .background(
                    if (isMyMessage) accent else background_gray100,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(6.dp)
        ) {
            Text(
                text = message.content,
                fontFamily = poppins,
                color = if (isMyMessage) white else typo_gray200,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = message.time,
                color = if (isMyMessage) white else typo_gray200,
                fontFamily = poppins,
                fontSize = 10.sp
            )
        }
    }
}
