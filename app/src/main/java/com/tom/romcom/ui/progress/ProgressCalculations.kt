package com.tom.romcom.ui.progress

import com.tom.romcom.data.StudySession
import java.time.LocalDate

fun expectedHours(
    startDay: Long,
    endDay: Long,
    credits: Double,
    hoursPerCredit: Int,
    today: Long = LocalDate.now().toEpochDay()
): Double {
    val totalHours = credits * hoursPerCredit
    val totalDays = endDay - startDay
    if (totalDays <= 0) return totalHours
    val daysPassed = today - startDay
    val fractionOfDays = (daysPassed.toDouble() / totalDays).coerceIn(0.0, 1.0)
    return totalHours * fractionOfDays
}

fun hoursDonePerCourse(
    courseId: Int,
    sessions: List<StudySession>
): Double {
    val millisDone = sessions.filter { courseId == it.courseId }
        .sumOf { (it.endTime ?: it.startTime) - it.startTime }
    return millisDone / 3_600_000.0
}