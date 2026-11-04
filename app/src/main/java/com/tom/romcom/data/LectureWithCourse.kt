package com.tom.romcom.data

data class LectureWithCourse(
    val startMinute: Int,
    val endMinute: Int,
    val room: String,
    val courseName: String,
    val epochDay: Long,
    val color: Long,
    val profName: String
)
