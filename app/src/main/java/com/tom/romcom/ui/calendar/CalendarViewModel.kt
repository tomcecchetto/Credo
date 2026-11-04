package com.tom.romcom.ui.calendar

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.tom.romcom.data.CredoDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

@OptIn(ExperimentalCoroutinesApi::class)
class CalendarViewModel(application: Application) : AndroidViewModel(application){

    private val dataBase = CredoDatabase.getDatabase(application)
    private val lectureDao = dataBase.lectureDao()
    private val _selectedDay = MutableStateFlow(LocalDate.now().toEpochDay())
    private val slotDao = dataBase.recurringSlotDao()
    private val _selectedView = MutableStateFlow(CalendarView.DAY)
    val selectedDay: StateFlow<Long> = _selectedDay
    val selectedView: StateFlow<CalendarView> = _selectedView
    val slots = slotDao.getAllSlots()
    val lectures = _selectedDay.flatMapLatest { day -> lectureDao.getLecturesForDay(day) }

    val monthLectures = _selectedDay.flatMapLatest { day ->
        val startDay = monthGridStart(day)
        val endDay = startDay + 41
        lectureDao.getLecturesBetween(startDay, endDay) }

    fun previousDay() {
        _selectedDay.value -= 1
    }

    fun nextDay() {
        _selectedDay.value += 1
    }

    fun goToToday() {
        _selectedDay.value = LocalDate.now().toEpochDay()
    }

    fun selectView(view: CalendarView){
        _selectedView.value = view
    }
}
