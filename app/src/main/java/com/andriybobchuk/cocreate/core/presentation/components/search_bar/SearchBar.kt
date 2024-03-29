package com.andriybobchuk.cocreate.core.presentation.components.search_bar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andriybobchuk.cocreate.R
import com.andriybobchuk.cocreate.ui.theme.background_gray200
import com.andriybobchuk.cocreate.ui.theme.typo_gray100
import com.andriybobchuk.cocreate.ui.theme.typo_gray200
import com.andriybobchuk.cocreate.ui.theme.white

@Composable
fun SearchBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onCancelSearch: () -> Unit
) {
    Row(
        modifier = Modifier
            .height(50.dp)
            .background(white),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .padding(start = 10.dp, end = 8.dp)
                .background(background_gray200, RoundedCornerShape(14.dp))
                .padding(8.dp)
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = "Search",
                tint = typo_gray100
            )
//            if (searchQuery.isEmpty()) {
//                Text(
//                    text = "Search tools, profession, city",
//                    fontSize = 14.sp,
//                    color = Color.Gray,
//                    modifier = Modifier.padding(start = 8.dp)
//                )
//            }
            BasicTextField(
                value = searchQuery,
                onValueChange = { newValue -> onSearchQueryChange(newValue) },
                textStyle = TextStyle(color = typo_gray200, fontSize = 16.sp),
                singleLine = true,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .fillMaxWidth()
            )
        }

        IconButton(
            onClick = { onCancelSearch() },
            modifier = Modifier.padding(end = 16.dp),
        ) {
            Text(
                text = "Cancel",
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
            )
        }
    }
}