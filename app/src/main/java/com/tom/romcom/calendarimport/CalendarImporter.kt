package com.tom.romcom.calendarimport

import com.tom.romcom.data.Lecture
import com.tom.romcom.data.LectureDao
import com.tom.romcom.data.RecurringSlot
import com.tom.romcom.data.RecurringSlotDao

class CalendarImporter(
    private val slotDao: RecurringSlotDao,
    private val lectureDao: LectureDao
) {
    suspend fun importFile(text: String) {
        val events = parseIcs(text)
        val groups = events.groupBy {
            Triple(
                it.seriesId,
                it.start.hour * 60 + it.start.minute,
                it.start.dayOfWeek.value
            )
        }

        val lectures = mutableListOf<Lecture>()
        for ((key, slotEvents) in groups) {
            val (seriesId, startMinute, dayOfWeek) = key
            val newId = slotDao.insert(
                slot = RecurringSlot(
                    seriesId = seriesId,
                    startMinuteOfDay = startMinute,
                    dayOfWeek = dayOfWeek
                )
            ).toInt()
            val slotId = if (newId != -1) newId else slotDao.findBySeriesDayStart(
                seriesId = seriesId,
                startMinuteOfDay = startMinute,
                dayOfWeek = dayOfWeek
            )?.id ?: error("Slot $seriesId / $dayOfWeek / $startMinute not found after insert")
            for (event in slotEvents) {
                lectures.add(
                    Lecture(
                        room = event.location,
                        startMinuteOfDay = event.start.hour * 60 + event.start.minute,
                        endMinuteOfDay = event.end.hour * 60 + event.end.minute,
                        calendarUid = event.uid,
                        epochDay = event.start.toLocalDate().toEpochDay(),
                        slotId = slotId
                    )
                )
            }
        }
        lectureDao.insertAll(lectures)
    }
}
