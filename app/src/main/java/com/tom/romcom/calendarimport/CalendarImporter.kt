package com.tom.romcom.calendarimport

import androidx.room.withTransaction
import com.tom.romcom.data.CredoDatabase
import com.tom.romcom.data.Lecture
import com.tom.romcom.data.LectureDao
import com.tom.romcom.data.RecurringSlot
import com.tom.romcom.data.RecurringSlotDao
import com.tom.romcom.data.Semester
import com.tom.romcom.data.SemesterDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CalendarImporter(
    private val slotDao: RecurringSlotDao,
    private val lectureDao: LectureDao,
    private val semesterDao: SemesterDao,
    private val database: CredoDatabase,
) {
    suspend fun importFile(text: String): Int {
        val events = withContext(Dispatchers.Default) { parseIcs(text) }
        val groups = withContext(Dispatchers.Default) {
            events.groupBy {
                Triple(
                    it.seriesId,
                    it.start.hour * 60 + it.start.minute,
                    it.start.dayOfWeek.value
                )
            }
        }
        database.withTransaction {

            val lectures = mutableListOf<Lecture>()
            for ((key, slotEvents) in groups) {
                val (seriesId, startMinute, dayOfWeek) = key
                val newId = slotDao.insert(
                    slot = RecurringSlot(
                        seriesId = seriesId,
                        startMinuteOfDay = startMinute,
                        dayOfWeek = dayOfWeek,
                        title = slotEvents.first().summary
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
            val existing = semesterDao.getCurrentSemesterOnce()
            if (existing == null) {
                val regularEvents = groups
                    .filter { it.value.size >= 3 }
                    .flatMap { it.value }
                val startDay = regularEvents.minOfOrNull { it.start.toLocalDate().toEpochDay() }
                val endDay = regularEvents.maxOfOrNull { it.end.toLocalDate().toEpochDay() }
                if (startDay != null && endDay != null) {
                    val sem = Semester(
                        startEpochDay = startDay,
                        endEpochDay = endDay,
                        hoursPerCredit = 30,
                        lectureWeeks = (((endDay - startDay) / 7) + 1).toInt()
                    )
                    semesterDao.insert(sem)
                }
            }
        }
        return events.size
    }
}
