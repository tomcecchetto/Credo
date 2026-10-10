package com.tom.romcom.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    version = 1,
    entities = [Lecture::class, Task::class, Course::class, StudySession::class, Semester::class],
    exportSchema = false
)
abstract class CredoDatabase : RoomDatabase() {

    abstract fun courseDao() : CourseDao

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