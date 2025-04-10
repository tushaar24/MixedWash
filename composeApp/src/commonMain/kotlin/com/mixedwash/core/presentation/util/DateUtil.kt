package com.mixedwash.core.presentation.util

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.toLocalDateTime

fun Long.formatHour(): Pair<String, String> {
    val instant = Instant.fromEpochSeconds(this)
    val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    val hour = localDateTime.hour % 12
    val minute = localDateTime.minute
    val period = if (localDateTime.hour < 12) "am" else "pm"
    val formattedTime = run {
        val h = (if (hour == 0) 12 else hour).toString()
        val m = minute.toString().padStart(2, '0')
        "$h"
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

/*
    accepts a Long timestamp(seconds since epoch), and return the date-time
     like so: 27 Mar • 12:00 pm
 */
fun formatTimestamp(timestamp: Long): String {
    val instant = Instant.fromEpochSeconds(timestamp)
    val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())

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

/**
 * Returns string like "9 am - 11 am"
 * */
inline fun formattedHourTime(startEpochSeconds: Long, endEpochSeconds: Long): String {
    val startDateTime = Instant.fromEpochSeconds(startEpochSeconds).toLocalDateTime(TimeZone.currentSystemDefault())
    val endDateTime = Instant.fromEpochSeconds(endEpochSeconds).toLocalDateTime(TimeZone.currentSystemDefault())

   val startDateTimeString = startDateTime.run {
        val hour = if (hour % 12 == 0) 12 else hour % 12
        val amPm = if (hour < 12) "am" else "pm"
        "$hour $amPm"
    }
    val endDateTimeString = endDateTime.run {
        val hour = if (hour % 12 == 0) 12 else hour % 12
        val amPm = if (hour < 12) "am" else "pm"
        "$hour $amPm"
    }

    return "$startDateTimeString - $endDateTimeString"
}


inline fun getDayOfWeekAbbrev(epochSeconds: Long): String {
    val instant = Instant.fromEpochSeconds(epochSeconds)
    val dayOfWeek = instant.toLocalDateTime(TimeZone.currentSystemDefault()).dayOfWeek
    val dayOfWeekAbbreviations = listOf(
        "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"
    )
    return dayOfWeekAbbreviations[dayOfWeek.isoDayNumber - 1]
}