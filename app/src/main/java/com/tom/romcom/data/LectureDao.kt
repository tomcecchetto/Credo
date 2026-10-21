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
}
