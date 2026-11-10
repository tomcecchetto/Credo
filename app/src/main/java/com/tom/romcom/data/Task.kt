package com.tom.romcom.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

enum class TaskType {
    HOMEWORK, PROJECT, LAB
}

@Entity(
    foreignKeys = [ForeignKey(
        entity = Course::class,
        parentColumns = ["id"],
        childColumns = ["courseId"],
        onDelete = ForeignKey.SET_NULL
    )],
    indices = [Index("courseId")]
)

data class Task(
    val courseId: Int? = null,
    val taskType: TaskType,
    val title: String,
    val notes: String?,
    val dueEpochDay: Long,
    val dueMinuteOfDay: Int? = null,
    val isCompleted: Boolean = false,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)


