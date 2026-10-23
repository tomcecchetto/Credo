package com.tom.romcom.ui.calendar

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tom.romcom.calendarimport.CalendarImporter
import com.tom.romcom.data.Course
import com.tom.romcom.data.CredoDatabase
import com.tom.romcom.data.RecurringSlot
import com.tom.romcom.data.Semester
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class CalendarViewModel(application: Application) : AndroidViewModel(application = application) {

    private val db = CredoDatabase.getDatabase(context = application)
    private val slotDao = db.recurringSlotDao()
    private val lectureDao = db.lectureDao()
    private val courseDao = db.courseDao()
    private val semesterDao = db.semesterDao()

    val lectures = lectureDao.getAllLectures()
    val slotsWithInfo = slotDao.getSlotsWithInfo()
    val slots = slotDao.getAllSlots()
    val semester: Flow<Semester?> = semesterDao.getCurrentSemester()
    private val importer = CalendarImporter(slotDao, lectureDao)

    val courses: Flow<List<Course>> = semester.flatMapLatest { current ->
        if (current == null) flowOf(emptyList())
        else courseDao.getCoursesForSemester(current.id)
    }

    fun importCalendar(text: String) {
        viewModelScope.launch {
            importer.importFile(text)
        }
    }

    fun setAttending(slot: RecurringSlot, attending: Boolean) {
        val slotCopy = slot.copy(attending = attending)
        viewModelScope.launch { slotDao.update(slotCopy) }
    }

    fun setCourseId(slot: RecurringSlot, courseId: Int?) {
        val slotCopy = slot.copy(courseId = courseId)
        viewModelScope.launch { slotDao.update(slotCopy) }
    }
}
