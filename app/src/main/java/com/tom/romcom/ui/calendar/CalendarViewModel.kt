package com.tom.romcom.ui.calendar

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tom.romcom.calendarimport.CalendarImporter
import com.tom.romcom.data.CredoDatabase
import com.tom.romcom.data.RecurringSlot
import kotlinx.coroutines.launch

class CalendarViewModel(application: Application) : AndroidViewModel(application = application) {

    private val db = CredoDatabase.getDatabase(context = application)
    private val slotDao = db.recurringSlotDao()
    private val lectureDao = db.lectureDao()

    val lectures = lectureDao.getAllLectures()
    val slotsWithInfo = slotDao.getSlotsWithInfo()
    val slots = slotDao.getAllSlots()
    private val importer = CalendarImporter(slotDao, lectureDao)

    fun importCalendar(text: String) {
        viewModelScope.launch {
            importer.importFile(text)
        }
    }

    fun setAttending(slot: RecurringSlot, attending: Boolean) {
        val slotCopy = slot.copy(attending = attending)
        viewModelScope.launch { slotDao.update(slotCopy) }
    }
}
