package com.andriybobchuk.cocreate.core.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Snackbar
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.andriybobchuk.cocreate.ui.theme.*

@Composable
fun ccSnackbar(
    message: String,
    messageType: MessageType = MessageType.Info,
    actionText: String? = null,
    onActionClick: () -> Unit = {}
) {
    val backgroundColor = when (messageType) {
        MessageType.Warning -> orange
        MessageType.Error -> red
        MessageType.Success -> green
        else -> accent // Default color for info
    }

//    val textColor = if (messageType == MessageType.Warning || messageType == MessageType.Error) {
//        white
//    } else {
//        title_black
//    }

    val textColor = white

    // Define the Snackbar layout and behavior
    Snackbar(
        modifier = Modifier.padding(vertical = 16.dp),
        action = {
            actionText?.let {
                TextButton(
                    onClick = { onActionClick() }
                ) {
                    Text(text = actionText, color = textColor, fontFamily = poppins)
                }
            }
        },
        backgroundColor = backgroundColor
    ) {
        Text(text = message, color = textColor, fontFamily = poppins)
    }
}

enum class MessageType {
    Info, // Default
    Warning,
    Error,
    Success
}