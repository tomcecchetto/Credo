package com.tom.romcom.ui.calendar

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tom.romcom.R
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
    var importMessage by mutableStateOf<String?>(null)
        private set
    private val importer = CalendarImporter(slotDao, lectureDao, db)

    val courses: Flow<List<Course>> = semester.flatMapLatest { current ->
        if (current == null) flowOf(emptyList())
        else courseDao.getCoursesForSemester(current.id)
    }

    fun importCalendar(text: String) {
        viewModelScope.launch {
            try {
                val count = importer.importFile(text)
                importMessage = if (count > 0) getApplication<Application>().resources.getQuantityString(R.plurals.lectures, count, count)
                else getApplication<Application>().getString(R.string.no_events_found)
            } catch (e: Exception) {
                Log.e("Credo", "Import failed", e)
                importMessage = getApplication<Application>().getString(R.string.import_failed)
            }
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
