package com.tom.romcom.calendarimport

import org.junit.Assert.assertEquals
import org.junit.Test

class IcsParserTest {

    @Test
    fun parsesAllEventsFromFile() {

        val text = javaClass.classLoader!!.getResource("demo_semester.ics").readText()
        val result = parseIcs(text)
        assertEquals(156, result.size)
    }

    @Test
    fun dateWorks() {
        val text = javaClass.classLoader!!.getResource("demo_semester.ics").readText()
        val result = parseIcs(text)
        val first = result.first { it.uid == "4011000720261021000106@EXAMPLE.CH" }
        assertEquals(15, first.start.hour)
        assertEquals(0, first.start.minute)
    }

    @Test
    fun timeAfterClockChange() {
        val text = javaClass.classLoader!!.getResource("demo_semester.ics").readText()
        val result = parseIcs(text)
        val first = result.first { it.uid == "4011000720261028000107@EXAMPLE.CH" }
        assertEquals(15, first.start.hour)
        assertEquals(0, first.start.minute)
    }
}
