package com.tom.romcom.ui.tasks

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.tom.romcom.R
import com.tom.romcom.ui.formatDayHeader
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun dueLabelText(
    dueLabel: DueLabel,
    dueEpochDay: Long
): String = when (dueLabel) {
    DueLabel.YESTERDAY -> stringResource(R.string.yesterday)
    DueLabel.TODAY -> stringResource(R.string.today)
    DueLabel.TOMORROW -> stringResource(R.string.tomorrow)
    DueLabel.WEEKDAY -> LocalDate.ofEpochDay(dueEpochDay).dayOfWeek.getDisplayName(
            TextStyle.SHORT, Locale.getDefault())
    DueLabel.DATE -> formatDayHeader(dueEpochDay)
}
