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
    val selectedDay: StateFlow<Long> = _selectedDay

    val lectures = _selectedDay.flatMapLatest { day -> lectureDao.getLecturesForDay(day) }
}