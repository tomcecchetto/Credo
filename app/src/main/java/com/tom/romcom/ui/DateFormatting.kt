package com.tom.romcom.ui

import androidx.compose.ui.res.stringResource
import com.tom.romcom.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

fun formatEpochDay(epochDay: Long?): String {
    val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
    return if (epochDay != null) LocalDate.ofEpochDay(epochDay).format(formatter) else ""
}