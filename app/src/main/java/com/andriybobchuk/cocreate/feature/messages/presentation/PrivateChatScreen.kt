package com.andriybobchuk.cocreate.feature.messages.presentation

import androidx.compose.foundation.Image
import com.andriybobchuk.cocreate.R
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
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
import com.andriybobchuk.cocreate.core.presentation.components.Avatar
import com.andriybobchuk.cocreate.feature.messages.domain.model.FullPrivateChat
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
    viewModel.subscribeToRealtimeUpdates(chatId)
    val chatData = viewModel.state.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(white)
    ) {
        ChatRoomHeader(
            chatData = chatData,
            onBackClick = { navController.popBackStack() },
            onProfileClick = {
                navController.navigate(
                    "detail/{user}"
                        .replace(
                            oldValue = "{user}",
                            newValue = chatData.recipientData.uid,
                        ),
                )
            }
        )
        // Messages
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            val listState = rememberLazyListState()

            LaunchedEffect(chatData) {
                // Scroll to the last item when the message list changes
                listState.animateScrollToItem(chatData.messages.size)
            }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .padding(horizontal = 14.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp),
                state = listState // Apply the list state
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    value = messageTextState.value,
                    onValueChange = { newText -> messageTextState.value = newText },
                    textStyle = TextStyle(color = title_black, fontFamily = poppins),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                        .border(1.dp, background_gray200, RoundedCornerShape(16.dp))
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

        }

    }

}

@Composable
fun ChatRoomHeader(
    chatData: FullPrivateChat,
    onBackClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .background(white)
            .clickable { onProfileClick() }
            .padding(top = 4.dp)
    ) {
        IconButton(
            onClick = { onBackClick() },
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow),
                contentDescription = "Back",
                tint = title_black
            )
        }
        Spacer(modifier = Modifier.width(5.dp))
        Column(modifier = Modifier.wrapContentHeight()) {
            Avatar(radius = 36.dp, font = 16.sp, avatarUrl = chatData.recipientData.avatar, name = chatData.recipientData.name)
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
}

@Composable
fun MessageItem(message: Message) {
    val isMyMessage = message.isMyMessage // Determine whether the message is mine
    var isClicked by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = if (isMyMessage) Alignment.End else Alignment.Start
    ) {
        Column(
            modifier = Modifier
                .padding(
                    start = (if (isMyMessage) 64.dp else 0.dp),
                    end = (if (isMyMessage) 0.dp else 64.dp))
                .clickable { isClicked = !isClicked }
                .background(
                    if (isMyMessage) accent else background_gray100,
                    shape = RoundedCornerShape(
                        topStart = 18.dp,
                        topEnd = 18.dp,
                        bottomStart = if (isMyMessage) 18.dp else 4.dp,
                        bottomEnd = if (isMyMessage) 4.dp else 18.dp
                    )
                )
                .padding(8.dp)
        ) {
            Text(
                text = message.content,
                fontFamily = poppins,
                color = if (isMyMessage) white else typo_gray200,
                fontSize = 14.sp
            )
            if (isClicked) {
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
}
