package com.tom.romcom.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "semester")
data class Semester(
    val startEpochDay: Long,
    val endEpochDay: Long,
    val hoursPerCredit: Int,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)

