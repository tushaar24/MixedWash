package com.mixedwash.core.presentation.util

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun Long.formatTime(): Pair<String, String> {
    val instant = Instant.fromEpochSeconds(this)
    val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    val hour = localDateTime.hour % 12
    val minute = localDateTime.minute
    val period = if (localDateTime.hour < 12) "AM" else "PM"
    val formattedTime = run {
        val h = (if (hour == 0) 12 else hour).toString().padStart(2, '0')
        val m = minute.toString().padStart(2, '0')
        "$h:$m"
    }
    return Pair(formattedTime, period)
}

fun Long.getDayAndDate(): Pair<String, String> {
    val instant = Instant.fromEpochSeconds(this)
    val localDate = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
    val dayOfWeek = localDate.dayOfWeek.name.take(3)
    val dayOfMonth = localDate.dayOfMonth.toString().padStart(2, '0')
    return Pair(dayOfWeek, dayOfMonth)
}

fun Long.getMonth(): String {
    val instant = Instant.fromEpochSeconds(this)
    val localDate = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
    return localDate.month.name.take(3)
}

fun formatTimestamp(timestamp: Long): String {
    val instant = Instant.fromEpochMilliseconds(timestamp)
    val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault()) // Change to your desired timezone

    val monthAbbreviations = listOf(
        "Jan", "Feb", "Mar", "Apr", "May", "Jun",
        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    )

    val day = dateTime.dayOfMonth
    val month = monthAbbreviations[dateTime.monthNumber - 1]

    val hour = if (dateTime.hour % 12 == 0) 12 else dateTime.hour % 12
    val minutes = dateTime.minute.toString().padStart(2, '0')
    val amPm = if (dateTime.hour < 12) "am" else "pm"

    return "$day $month • $hour:$minutes $amPm"
}
