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
    indices = [Index("courseId")]
)
data class StudySession(
    val courseId: Int,
    val startTime: Long,
    val endTime: Long?,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)