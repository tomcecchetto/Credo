package com.tom.romcom.ui.progress

import com.tom.romcom.data.Course
import com.tom.romcom.data.Semester
import com.tom.romcom.data.StudySession
import java.time.LocalDate
import java.time.DayOfWeek
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters

fun expectedHours(
    startDay: Long,
    endDay: Long,
    totalHours: Double,
    today: Long = LocalDate.now().toEpochDay()
): Double {
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

fun classHoursForCourse(
    course: Course,
    lectureWeeks: Int,
    importedHours: Double?
): Double {
    val manualHours = (course.lectureHoursPerWeek * lectureWeeks) + (course.praktikumSessions * course.praktikumHoursPerSession)
    return importedHours ?: manualHours
}

fun hoursThisWeek(
    sessions: List<StudySession>,
    today: LocalDate = LocalDate.now()
): Double {
    val mondayMillis = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        .atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    val millisDone = sessions.filter { it.startTime >= mondayMillis }
        .sumOf { (it.endTime ?: it.startTime) - it.startTime}
    return millisDone / 3_600_000.0
}

fun isWeeklyGoalReached(
    course: Course,
    sessions: List<StudySession>,
    today: LocalDate = LocalDate.now()
): Boolean {
    val weekHours = hoursThisWeek(sessions.filter { it.courseId == course.id }, today)
    return (weekHours >= course.plannedHoursPerWeek && course.plannedHoursPerWeek > 0.0)
}
data class PlanProgress(
    val planTotalHours: Double,
    val planExpectedHours: Double,
    val hoursDone: Double
)
fun planProgressFor(
    course: Course,
    semester: Semester,
    sessions: List<StudySession>,
    today: Long = LocalDate.now().toEpochDay()
): PlanProgress {
    val hoursDone = hoursDonePerCourse(courseId = course.id, sessions = sessions)
    val planTotalHours = course.plannedHoursPerWeek * semester.lectureWeeks
    val planExpectedHours = expectedHours(
        startDay = semester.startEpochDay,
        endDay = semester.endEpochDay,
        totalHours = planTotalHours,
        today = today
    )
    return PlanProgress(hoursDone = hoursDone, planTotalHours = planTotalHours, planExpectedHours = planExpectedHours)
}
