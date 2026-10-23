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
        onDelete = ForeignKey.SET_NULL
    )],
    indices = [Index(value = ["courseId"]), Index(value = ["seriesId", "dayOfWeek", "startMinuteOfDay"], unique = true)]
)
data class RecurringSlot(
    val seriesId: String,
    val startMinuteOfDay: Int,
    val dayOfWeek: Int,                             // 1 = Monday, 2 = Tuesday etc
    @ColumnInfo(defaultValue = "") val title: String,
    val courseId: Int? = null,
    val attending: Boolean = false,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)
