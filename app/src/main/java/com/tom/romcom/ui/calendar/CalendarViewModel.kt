package com.tom.romcom.ui.calendar

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.tom.romcom.data.CredoDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class CalendarViewModel(application: Application) : AndroidViewModel(application){

    private val dataBase = CredoDatabase.getDatabase(application)
    private val lectureDao = dataBase.lectureDao()
    private val _selectedDay = MutableStateFlow(LocalDate.now().toEpochDay())
    private val slotDao = dataBase.recurringSlotDao()
    val selectedDay: StateFlow<Long> = _selectedDay
    val slots = slotDao.getAllSlots()
    val lectures = _selectedDay.flatMapLatest { day -> lectureDao.getLecturesForDay(day) }

    fun previousDay() {
        _selectedDay.value -= 1
    }

    fun nextDay() {
        _selectedDay.value += 1
    }

    fun goToToday() {
        _selectedDay.value = LocalDate.now().toEpochDay()
    }
}
