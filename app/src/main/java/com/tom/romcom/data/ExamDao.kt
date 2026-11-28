package com.tom.romcom.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ExamDao{

    @Query("SELECT * FROM Exam")
    suspend fun getAllExamsOnce(): List<Exam>

    @Query("SELECT * FROM Exam WHERE courseId = :courseId ORDER BY epochDay")
    fun getExamsForCourse(courseId: Int): Flow<List<Exam>>
    @Query("SELECT Exam.* FROM Exam INNER JOIN Course ON Exam.courseId = Course.id WHERE Course.semesterId = :semesterId ORDER BY Exam.epochDay")
    fun getExamsForSemester(semesterId: Int): Flow<List<Exam>>

    @Query("SELECT Exam.*, " +
            "Course.name AS courseName, " +
            "Course.color AS courseColor " +
            "FROM Exam " +
            "JOIN Course ON Exam.courseId = Course.id " +
            "WHERE Exam.epochDay = :day " +
            "ORDER BY COALESCE(Exam.startMinuteOfDay, 1440)")
    fun getExamsForDay(day: Long): Flow<List<ExamWithCourse>>

    @Query("SELECT Exam.*, " +
            "Course.name AS courseName, " +
            "Course.color AS courseColor " +
            "FROM Exam " +
            "JOIN Course ON Exam.courseId = Course.id " +
            "WHERE Exam.epochDay >= :today " +
            "ORDER BY Exam.epochDay, COALESCE(Exam.startMinuteOfDay, 1440)")
    fun getRemainingExams(today: Long): Flow<List<ExamWithCourse>>

    @Query("SELECT Exam.*, " +
            "Course.name AS courseName, " +
            "Course.color AS courseColor " +
            "FROM Exam " +
            "JOIN Course ON Exam.courseId = Course.id " +
            "WHERE Exam.id = :id")
    suspend fun getExamById(id: Int): ExamWithCourse?

    @Query("DELETE FROM Exam")
    suspend fun deleteAllExams()

    @Insert
    suspend fun insertExams(exams: List<Exam>)

    @Insert
    suspend fun insert(exam: Exam)

    @Update
    suspend fun update(exam: Exam)

    @Delete
    suspend fun delete(exam: Exam)
}
