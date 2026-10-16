package com.tom.romcom.ui

import java.text.NumberFormat
import java.time.LocalDate
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
