package com.tom.romcom.ui.calendar

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

fun monthGridStart(day: Long): Long {
    val startDay = LocalDate.ofEpochDay(day).withDayOfMonth(1).with(
        TemporalAdjusters.previousOrSame(
            DayOfWeek.MONDAY
        )
    ).toEpochDay()
    return startDay
}
