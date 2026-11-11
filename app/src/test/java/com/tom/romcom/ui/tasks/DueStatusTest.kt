package com.tom.romcom.ui.tasks

import org.junit.Test
import java.time.LocalDate
import org.junit.Assert.assertEquals

class DueStatusTest {
    val today: Long = LocalDate.of(2026, 11, 11).toEpochDay()
    @Test
    fun dueToday() {
        assertEquals(DueStatus(Urgency.SOON, DueLabel.TODAY), dueStatusFor(dueEpochDay = today, today = today))
    }
    @Test
    fun dueTomorrow() {
        assertEquals(DueStatus(Urgency.SOON, DueLabel.TOMORROW), dueStatusFor(dueEpochDay = today + 1, today = today))
    }
    @Test
    fun dueYesterday() {
        assertEquals(DueStatus(Urgency.OVERDUE, DueLabel.YESTERDAY), dueStatusFor(dueEpochDay = today - 1, today = today))
    }
    @Test
    fun dueSameWeekButPast() {
        assertEquals(DueStatus(Urgency.OVERDUE, DueLabel.DATE), dueStatusFor(dueEpochDay = today - 2, today = today))
    }
    @Test
    fun dueSoon() {
        assertEquals(DueStatus(Urgency.SOON, DueLabel.WEEKDAY), dueStatusFor(dueEpochDay = today + 2, today = today))
    }
    @Test
    fun lastOrangeDay() {
        assertEquals(DueStatus(Urgency.SOON, DueLabel.WEEKDAY), dueStatusFor(dueEpochDay = today + 3, today = today))
    }
    @Test
    fun firstGreyDay() {
        assertEquals(DueStatus(Urgency.LATER, DueLabel.WEEKDAY), dueStatusFor(dueEpochDay = today + 4, today = today))
    }
    @Test
    fun nextWeek() {
        assertEquals(DueStatus(Urgency.LATER, DueLabel.DATE), dueStatusFor(dueEpochDay = today + 5, today = today))
    }
    @Test
    fun differentToday() {
        assertEquals(DueStatus(Urgency.SOON, DueLabel.DATE), dueStatusFor(dueEpochDay = LocalDate.of(2026, 11, 16).toEpochDay(), today = LocalDate.of(2026, 11, 14).toEpochDay()))
    }
}
