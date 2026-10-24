package com.tom.romcom.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LectureDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(lectures: List<Lecture>): List<Long>

    @Query("SELECT * FROM Lecture ORDER BY epochDay, startMinuteOfDay")
    fun getAllLectures(): Flow<List<Lecture>>

    @Query("SELECT RecurringSlot.courseId AS courseId, " +
            "SUM(Lecture.endMinuteOfDay - Lecture.startMinuteOfDay) / 60.0 AS classHours " +
            "FROM Lecture " +
            "JOIN RecurringSlot ON RecurringSlot.id = Lecture.slotId " +
            "WHERE Lecture.isHappening = 1 " +
            "AND RecurringSlot.attending = 1 " +
            "AND RecurringSlot.courseId IS NOT NULL " +
            "GROUP BY RecurringSlot.courseId")
    fun getClassHoursPerCourse() : Flow<List<CourseClassHours>>
}
