package com.tom.romcom.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SemesterDao {

    @Query("SELECT * FROM semester ORDER BY startEpochDay DESC LIMIT 1")
    fun getCurrentSemester(): Flow<Semester?>

    @Query("SELECT * FROM semester ORDER BY startEpochDay DESC LIMIT 1")
    suspend fun getCurrentSemesterOnce(): Semester?

    @Insert
    suspend fun insert(semester: Semester)

    @Update
    suspend fun update(semester: Semester)
}
