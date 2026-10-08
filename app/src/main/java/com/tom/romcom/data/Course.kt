package com.tom.romcom.data

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
    val name: String,
    val professor: String,
    val credits: Double,
    val color: Long,
    val semesterId: Int,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)