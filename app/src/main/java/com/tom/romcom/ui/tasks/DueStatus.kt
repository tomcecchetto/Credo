package com.tom.romcom.ui.tasks

import java.time.DayOfWeek
import java.time.LocalDate

enum class Urgency {
    OVERDUE, SOON, LATER
}
enum class DueLabel {
    YESTERDAY, TODAY, TOMORROW, WEEKDAY, DATE
}
data class DueStatus(
    val level: Urgency,
    val day: DueLabel
)

fun dueStatusFor(
    dueEpochDay: Long,
    today: Long = LocalDate.now().toEpochDay()
): DueStatus {
    val daysUntilDueDate = dueEpochDay - today
    val todayMonday = LocalDate.ofEpochDay(today).with(DayOfWeek.MONDAY)
    val dueMonday = LocalDate.ofEpochDay(dueEpochDay).with(DayOfWeek.MONDAY)
    val dueLabel = when {
        daysUntilDueDate == 0L -> DueLabel.TODAY
        daysUntilDueDate == -1L -> DueLabel.YESTERDAY
        daysUntilDueDate == 1L -> DueLabel.TOMORROW
        (todayMonday == dueMonday) && daysUntilDueDate > 0 -> DueLabel.WEEKDAY
        else -> DueLabel.DATE
    }
    val urgency = when {
        daysUntilDueDate <= -1L -> Urgency.OVERDUE
        daysUntilDueDate > 3L -> Urgency.LATER
        else -> Urgency.SOON
    }
    return DueStatus(level = urgency, day = dueLabel)
}
