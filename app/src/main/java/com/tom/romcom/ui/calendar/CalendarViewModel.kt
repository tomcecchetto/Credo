package com.tom.romcom.ui.calendar

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.tom.romcom.data.CredoDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import java.time.LocalDate
import java.time.YearMonth

@OptIn(ExperimentalCoroutinesApi::class)
class CalendarViewModel(application: Application) : AndroidViewModel(application) {

    private val dataBase = CredoDatabase.getDatabase(application)
    private val lectureDao = dataBase.lectureDao()
    private val _selectedDay = MutableStateFlow(LocalDate.now().toEpochDay())
    private val slotDao = dataBase.recurringSlotDao()
    private val _selectedView = MutableStateFlow(CalendarView.DAY)
    val selectedDay: StateFlow<Long> = _selectedDay
    val selectedView: StateFlow<CalendarView> = _selectedView
    val slots = slotDao.getAllSlots()
    val lectures = _selectedDay.flatMapLatest { day -> lectureDao.getLecturesForDay(day) }

    private fun monthJump(direction: Long): Long {
        val newDate = LocalDate.ofEpochDay(_selectedDay.value).plusMonths(direction)
        return if (YearMonth.from(newDate) == YearMonth.from(LocalDate.now())) LocalDate.now()
            .toEpochDay() else newDate.withDayOfMonth(1).toEpochDay()
    }

    fun previous() {
        _selectedDay.value = when (_selectedView.value) {
            CalendarView.DAY -> _selectedDay.value - 1
            CalendarView.WEEK -> _selectedDay.value - 7
            CalendarView.MONTH -> monthJump(-1)
        }
    }

    fun next() {
        _selectedDay.value = when (_selectedView.value) {
            CalendarView.DAY -> _selectedDay.value + 1
            CalendarView.WEEK -> _selectedDay.value + 7
            CalendarView.MONTH -> monthJump(1)
        }
    }

    fun goToToday() {
        _selectedDay.value = LocalDate.now().toEpochDay()
    }

    fun selectView(view: CalendarView) {
        _selectedView.value = view
    }

    fun selectDay(day: Long) {
        _selectedDay.value = day
    }
}
