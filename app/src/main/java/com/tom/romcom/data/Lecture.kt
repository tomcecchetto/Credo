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
    ), ForeignKey(
        entity = RecurringSlot::class,
        parentColumns = ["id"],
        childColumns = ["slotId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["courseId"]), Index(value = ["calendarUid"], unique = true), Index(
        value = ["slotId"]
    )]
)
// import always sets slotId, manual form always sets courseId
data class Lecture(
    val courseId: Int? = null,
    val room: String,
    val epochDay: Long,
    val isHappening: Boolean = true,
    val isEditedByUser: Boolean = false,
    val startMinuteOfDay: Int,
    val endMinuteOfDay: Int,
    val calendarUid: String?,
    val slotId: Int? = null,
    @ColumnInfo(defaultValue = "1") val attended: Boolean = true,
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
)
