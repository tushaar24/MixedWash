package com.mixedwash.presentation.util

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
    val localDate = instant.toLocalDateTime(TimeZone.UTC).date
    val dayOfWeek = localDate.dayOfWeek.name.take(3)
    val dayOfMonth = localDate.dayOfMonth.toString().padStart(2, '0')
    return Pair(dayOfWeek, dayOfMonth)
}

fun Long.getMonth(): String {
    val instant = Instant.fromEpochSeconds(this)
    val localDate = instant.toLocalDateTime(TimeZone.UTC).date
    return localDate.month.name.take(3)
}