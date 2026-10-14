package com.tom.romcom.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = Course::class,
        parentColumns = ["id"],
        childColumns = ["courseId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["courseId"])]
)

data class Exam(
    val courseId: Int,
    val epochDay: Long,
    val startMinuteOfDay: Int?,
    val endMinuteOfDay: Int?,
    val room: String?,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)
