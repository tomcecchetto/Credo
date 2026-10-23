package com.tom.romcom.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface RecurringSlotDao {
    /** finds an existing slot by its unique key (series + weekday + start time).
     * Used by the import when insert() returned -1 because the slot already existed */
    @Query("SELECT * FROM RecurringSlot WHERE seriesId = :seriesId AND dayOfWeek = :dayOfWeek AND startMinuteOfDay = :startMinuteOfDay")
    suspend fun findBySeriesDayStart(
        seriesId: String,
        dayOfWeek: Int,
        startMinuteOfDay: Int
    ): RecurringSlot?

    @Query("SELECT * FROM RecurringSlot ORDER BY dayOfWeek, startMinuteOfDay")
    fun getAllSlots(): Flow<List<RecurringSlot>>

    @Query("SELECT RecurringSlot.*, " +
            "COUNT(Lecture.id) AS lectureCount," +
            "MAX(Lecture.endMinuteOfDay) AS endMinuteOfDay," +
            "MAX(Lecture.room) AS room " +
            "FROM RecurringSlot " +
            "JOIN Lecture ON Lecture.slotId = RecurringSlot.id " +
            "GROUP BY slotId " +
            "ORDER BY RecurringSlot.dayOfWeek, RecurringSlot.startMinuteOfDay")
    fun getSlotsWithInfo(): Flow<List<SlotWithInfo>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(slot: RecurringSlot): Long



    @Update
    suspend fun update(slot: RecurringSlot)
}
