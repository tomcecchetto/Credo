package com.tom.romcom.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("SELECT * FROM Task")
    suspend fun getAllTasksOnce(): List<Task>

    @Query("SELECT Task.*, " +
            "Course.name AS courseName, " +
            "Course.color AS color " +
            "FROM Task " +
            "LEFT JOIN Course ON Course.id = Task.courseId " +
            "WHERE Task.isCompleted = 0 " +
            "ORDER BY Task.dueEpochDay, COALESCE(Task.dueMinuteOfDay, 1440)")
    fun getOpenTasks(): Flow<List<TaskWithCourse>>

    @Query("SELECT Task.*, " +
            "Course.name AS courseName, " +
            "Course.color AS color " +
            "FROM Task " +
            "LEFT JOIN Course ON Course.id = Task.courseId " +
            "WHERE Task.isCompleted = 1 " +
            "ORDER BY Task.dueEpochDay DESC LIMIT 50")
    fun getCompletedTasks(): Flow<List<TaskWithCourse>>

    @Query("SELECT Task.* FROM Task WHERE Task.id = :id")
    suspend fun getTaskById(id: Int): Task?

    @Query("SELECT Task.*, " +
            "Course.name AS courseName, " +
            "Course.color AS color " +
            "FROM Task " +
            "LEFT JOIN Course ON Course.id = Task.courseId " +
            "WHERE Task.taskType IN ('PROJECT', 'LAB') " +
            "AND (Course.semesterId = :semesterId OR " +
            "(Task.courseId IS NULL AND Task.dueEpochDay >= :startDay)) " +
            "ORDER BY Task.dueEpochDay, COALESCE(Task.dueMinuteOfDay, 1440)")
    fun getDeadlines(startDay: Long, semesterId: Int): Flow<List<TaskWithCourse>>

    @Query("SELECT Task.* " +
            "FROM Task " +
            "LEFT JOIN Course ON Course.id = Task.courseId " +
            "WHERE Task.taskType = 'HOMEWORK' " +
            "AND (Course.semesterId = :semesterId OR " +
            "(Task.courseId IS NULL AND Task.dueEpochDay >= :startDay))")
    fun getExerciseDeadlines(semesterId: Int, startDay: Long): Flow<List<Task>>

    @Query("SELECT Task.*, " +
            "Course.name AS courseName, " +
            "Course.color AS color " +
            "FROM Task " +
            "LEFT JOIN Course ON Course.id = Task.courseId " +
            "WHERE Task.dueEpochDay = :day " +
            "ORDER BY Task.isCompleted, COALESCE(Task.dueMinuteOfDay, 1440)")
    fun getTasksForDay(day: Long): Flow<List<TaskWithCourse>>

    @Query("DELETE FROM Task")
    suspend fun deleteAllTasks()

    @Insert
    suspend fun insert(task: Task)

    @Insert
    suspend fun insertTasks(tasks: List<Task>)

    @Update
    suspend fun update(task: Task)

    @Query("UPDATE Task SET " +
            "title = :title, " +
            "taskType = :taskType, " +
            "courseId = :courseId, " +
            "notes = :notes, " +
            "dueMinuteOfDay = :dueMinuteOfDay " +
            "WHERE Task.seriesId = :seriesId AND Task.dueEpochDay >= :fromEpochDay")
    suspend fun updateThisAndFollowingTasks(
        title: String,
        taskType: TaskType,
        courseId: Int?,
        notes: String?,
        dueMinuteOfDay: Int?,
        seriesId: String,
        fromEpochDay: Long
    )

    @Delete
    suspend fun delete(task: Task)

    @Query("DELETE FROM Task WHERE Task.seriesId = :seriesId AND Task.dueEpochDay >= :fromEpochDay")
    suspend fun deleteThisAndFollowingTasks(seriesId: String, fromEpochDay: Long)

    @Delete
    suspend fun deleteTasks(tasks: List<Task>)
}
