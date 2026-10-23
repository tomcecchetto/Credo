package com.tom.romcom.data

import androidx.room.Embedded

data class SlotWithInfo(
    @Embedded val slot: RecurringSlot,
    val lectureCount: Int,
    val endMinuteOfDay: Int,
    val room: String
)
