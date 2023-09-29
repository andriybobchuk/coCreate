package com.andriybobchuk.cocreate.util

import android.os.Build
import android.text.format.DateUtils
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
fun getCurrentDateTime(): String {
    val currentDateTime = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("MMM d, ''yy 'at' h:mm a", Locale.ENGLISH)
    return currentDateTime.format(formatter)
}

fun toEpochMillis(dateTimeString: String): Long {
    // Define the date/time format used in getCurrentDateTime()
    val formatter = SimpleDateFormat("MMM d, ''yy 'at' h:mm a", Locale.ENGLISH)

    return try {
        // Parse the date/time string into a Date object
        val date = formatter.parse(dateTimeString)

        // Convert the Date object to epoch milliseconds
        date?.time ?: 0L
    } catch (e: Exception) {
        // Handle any parsing errors here
        0L
    }
}

fun formatShortTimeAgo(timestamp: String): String {
    val formatter = SimpleDateFormat("MMM d, ''yy 'at' h:mm a", Locale.ENGLISH)
    val date = formatter.parse(timestamp)
    if(date == null) {
        ccLog.e("UtilFunctions", "formatShortTimeAgo() failed to parse the string timestamp")
    }
    val currentTime = Calendar.getInstance().time
    val timeDifference = currentTime.time - date.time

    val seconds = timeDifference / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24
    val weeks = days / 7
    val months = days / 30
    val years = days / 365

    return when {
        seconds < 60 -> "${seconds}m"
        minutes < 60 -> "${minutes}m"
        hours < 24 -> "${hours}h"
        days < 7 -> "${days}d"
        weeks < 4 -> "${weeks}w"
        months < 12 -> "${months}mo"
        else -> "${years}y"
    }
}

fun generateShortUserDescription(position: String?, city: String?): String {
    fun capitalizeFirstLetter(str: String?): String {
        return str?.replaceFirstChar { it.uppercase() } ?: ""
    }
    return when {
        !position.isNullOrBlank() && !city.isNullOrBlank() ->
            "${capitalizeFirstLetter(position)} in ${capitalizeFirstLetter(city)}"
        !position.isNullOrBlank() ->
            capitalizeFirstLetter(position)
        !city.isNullOrBlank() ->
            "Based in ${capitalizeFirstLetter(city)}"
        else -> ""
    }
}