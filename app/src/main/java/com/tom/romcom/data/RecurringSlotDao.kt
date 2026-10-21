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

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(slot: RecurringSlot): Long

    @Query("SELECT * FROM RecurringSlot ORDER BY dayOfWeek, startMinuteOfDay")
    fun getAllSlots(): Flow<List<RecurringSlot>>

    @Update
    suspend fun update(slot: RecurringSlot)
}
