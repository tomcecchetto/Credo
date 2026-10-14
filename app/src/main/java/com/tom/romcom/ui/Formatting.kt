package com.tom.romcom.ui

import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

fun formatEpochDay(epochDay: Long?): String {
    val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
    return if (epochDay != null) LocalDate.ofEpochDay(epochDay).format(formatter) else ""
}

fun formatCredits(credits: Double): String {
    val formatter = NumberFormat.getNumberInstance()
    formatter.maximumFractionDigits = 2
    return formatter.format(credits)
}