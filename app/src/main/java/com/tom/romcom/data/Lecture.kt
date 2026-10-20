package com.tom.romcom.data

import androidx.room.ColumnInfo
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
    indices = [Index(value = ["courseId"]), Index(value = ["calendarUid"], unique = true)]
)

data class Lecture(
    val courseId: Int,
    val room: String,
    val epochDay: Long,
    val isHappening: Boolean = true,
    val isEditedByUser: Boolean = false,
    val startMinuteOfDay: Int,
    val endMinuteOfDay: Int,
    val calendarUid: String?,
    @ColumnInfo(defaultValue = "1") val attended: Boolean = true,
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
)
