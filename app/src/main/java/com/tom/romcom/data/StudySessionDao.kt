package com.tom.romcom.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface StudySessionDao {

    @Query("SELECT * FROM StudySession WHERE endTime IS NOT NULL ORDER BY startTime DESC")
    fun getFinishedSessions(): Flow<List<StudySession>>

    @Query("SELECT * FROM StudySession WHERE endTime IS NULL LIMIT 1")
    fun getRunningSession(): Flow<StudySession?>

    @Query("SELECT * FROM StudySession WHERE endTime IS NULL LIMIT 1")
    suspend fun getRunningSessionNow(): StudySession?

    @Transaction
    suspend fun startIfNoneRunning(session: StudySession) {
        if (getRunningSessionNow() == null) {
            insert(session)
        }
    }

    @Insert
    suspend fun insert(studySession: StudySession)

    @Update
    suspend fun update(studySession: StudySession)

    @Delete
    suspend fun delete(studySession: StudySession)
}
