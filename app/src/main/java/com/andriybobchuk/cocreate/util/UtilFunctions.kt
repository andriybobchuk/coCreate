package com.andriybobchuk.cocreate.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

fun getCurrentDateTime(): String {
    val currentDateTime = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("MMM d, ''yy 'at' h:mm a", Locale.ENGLISH)
    return currentDateTime.format(formatter)
}