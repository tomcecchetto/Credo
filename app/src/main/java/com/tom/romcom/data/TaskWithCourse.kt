package com.tom.romcom.data

import androidx.room.Embedded

data class TaskWithCourse(
    @Embedded val task: Task,
    val courseName: String?,
    val color: Long?
)