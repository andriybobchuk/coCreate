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

@RequiresApi(Build.VERSION_CODES.O)
fun formatMessageDateTime(dateTime: LocalDateTime): String {
    val currentDateTime = LocalDateTime.now()
    val diffMillis = currentDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() -
            dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

    return when {
        DateUtils.isToday(dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()) ->
            DateUtils.getRelativeTimeSpanString(dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                currentDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                DateUtils.MINUTE_IN_MILLIS).toString()

        DateUtils.isToday(dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() + DateUtils.DAY_IN_MILLIS) ->
            "Yesterday, ${SimpleDateFormat("HH:mm").format(dateTime)}"

        else ->
            SimpleDateFormat("HH:mm dd.MM.yy").format(dateTime)
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