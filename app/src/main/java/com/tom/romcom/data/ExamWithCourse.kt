package com.tom.romcom.data

import androidx.room.Embedded

data class ExamWithCourse (
    @Embedded val exam: Exam,
    val courseName: String,
    val courseColor: Long
)