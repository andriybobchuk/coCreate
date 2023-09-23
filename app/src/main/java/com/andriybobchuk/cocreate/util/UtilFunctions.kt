package com.andriybobchuk.cocreate.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDateTime
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