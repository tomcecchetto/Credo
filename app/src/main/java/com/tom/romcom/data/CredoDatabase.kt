package com.tom.romcom.data

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    version = 6,
    entities = [
        Lecture::class,
        Task::class,
        Course::class,
        StudySession::class,
        Semester::class,
        Exam::class,
        RecurringSlot:: class],
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3),
        AutoMigration(from = 3, to = 4),
        AutoMigration(from = 4, to = 5),
        AutoMigration(from = 5, to = 6)
    ]
)
abstract class CredoDatabase : RoomDatabase() {

    abstract fun courseDao() : CourseDao
    abstract fun semesterDao() : SemesterDao
    abstract fun examDao() : ExamDao
    abstract fun studySessionDao() : StudySessionDao
    abstract fun recurringSlotDao() : RecurringSlotDao
    abstract fun lectureDao() : LectureDao

    companion object {
        @Volatile
        private var INSTANCE: CredoDatabase? = null

        fun getDatabase(context: Context): CredoDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    CredoDatabase::class.java,
                    "credo_database"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
