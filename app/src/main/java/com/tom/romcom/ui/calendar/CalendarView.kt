package com.tom.romcom.ui.calendar

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.tom.romcom.R

enum class CalendarView {
    DAY, WEEK, MONTH
}

@Composable
fun viewLabel(view: CalendarView): String = when (view) {
    CalendarView.DAY -> stringResource(R.string.day)
    CalendarView.WEEK -> stringResource(R.string.week)
    CalendarView.MONTH -> stringResource(R.string.month)
}
