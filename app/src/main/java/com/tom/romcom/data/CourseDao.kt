package com.tom.romcom.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CourseDao {

    @Query("SELECT * FROM Course WHERE semesterId = :semesterId ORDER BY name")
    fun getCoursesForSemester(semesterId: Int): Flow<List<Course>>
    @Query("SELECT * FROM Course WHERE id = :courseId")
    suspend fun getCourse(courseId: Int): Course?
    @Query("DELETE FROM Course WHERE id = :courseId")
    suspend fun deleteById(courseId: Int)

    @Insert
    suspend fun insert(course: Course): Long

    @Update
    suspend fun update(course: Course)

    @Delete
    suspend fun delete(course: Course)
}