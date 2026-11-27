package com.tom.romcom.data

import kotlinx.serialization.Serializable

@Serializable
data class BackupFile(
    val dbVersion: Int,
    val exportEpochDay: Long,
    val semesters: List<Semester>,
    val courses: List<Course>,
    val recurringSlots: List<RecurringSlot>,
    val lectures: List<Lecture>,
    val exams: List<Exam>,
    val studySessions: List<StudySession>,
    val tasks: List<Task>
)
