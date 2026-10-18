package com.tom.romcom.ui

import java.text.NumberFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

fun formatEpochDay(epochDay: Long?): String {
    val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
    return if (epochDay != null) LocalDate.ofEpochDay(epochDay).format(formatter) else ""
}

fun formatCredits(credits: Double): String {
    val formatter = NumberFormat.getNumberInstance()
    formatter.maximumFractionDigits = 2
    return formatter.format(credits)
}

fun formatDuration(millis: Long): String {
    val seconds = millis / 1000
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val secs = seconds % 60
    return "%d:%02d:%02d".format(Locale.ROOT, hours, minutes, secs)
}

fun formatDateTime(millis: Long): String {
    val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT)
    return Instant.ofEpochMilli(millis)
        .atZone(ZoneId.systemDefault())
        .format(formatter)
}

fun formatMinuteOfDay(minute: Int?): String {
    if (minute == null) return ""
    val formatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
    return LocalTime.of(minute / 60, minute % 60).format(formatter)
}

fun formatHours(hours: Double): String {
    return "%.1f".format(hours)
}

fun formatHoursDiff(diff: Double): String {
    return "%+.1f".format(diff)
}