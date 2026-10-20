package com.tom.romcom.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = Semester::class,
        parentColumns = ["id"],
        childColumns = ["semesterId"],
        onDelete = ForeignKey.RESTRICT
    )],
    indices = [Index("semesterId")]
)
data class Course(
    val lectureHoursPerWeek: Double = 0.0,
    val name: String,
    val credits: Double,
    val color: Long,
    val semesterId: Int,
    @ColumnInfo(defaultValue = "0") val praktikumHoursPerSession: Double = 0.0,
    @ColumnInfo(defaultValue = "0") val praktikumSessions: Int = 0,
    @ColumnInfo(defaultValue = "0") val plannedHoursPerWeek: Double = 0.0,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)
