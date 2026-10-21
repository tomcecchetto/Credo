package com.tom.romcom.calendarimport

import org.junit.Assert.assertEquals
import org.junit.Test

class IcsParserTest {

    @Test
    fun parsesAllEventsFromFile() {

        val text = javaClass.classLoader!!.getResource("study_semester_calendar_booked.ics").readText()
        val result = parseIcs(text)
        assertEquals(271, result.size)
    }

    @Test
    fun dateWorks() {
        val text = javaClass.classLoader!!.getResource("study_semester_calendar_booked.ics").readText()
        val result = parseIcs(text)
        val first = result.first { it.uid == "5133448820261027000013@UZH.CH" }
        assertEquals(10, first.start.hour)
        assertEquals(15, first.start.minute)
    }
}
