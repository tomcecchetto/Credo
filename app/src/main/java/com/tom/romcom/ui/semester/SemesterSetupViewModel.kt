package com.tom.romcom.ui.semester

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tom.romcom.data.CredoDatabase
import com.tom.romcom.data.Semester
import com.tom.romcom.widget.updateAllWidgets
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


class SemesterSetupViewModel(application: Application) : AndroidViewModel(application) {

    private val semesterDao = CredoDatabase.getDatabase(application).semesterDao()
    private var loaded = false
    private var loadedSemester: Semester? = null
    var hoursPerCredit by mutableStateOf("")
        private set

    var startEpochDay by mutableStateOf<Long?>(null)
        private set
    var endEpochDay by mutableStateOf<Long?>(null)
        private set

    var lectureWeeks by mutableStateOf("14")
        private set

    fun onHoursChange(hours: String) {
        hoursPerCredit = hours
    }

    fun onStartDateSelected(millis: Long) {
        startEpochDay = TimeUnit.MILLISECONDS.toDays(millis)
    }

    fun onEndDateSelected(millis: Long) {
        endEpochDay = TimeUnit.MILLISECONDS.toDays(millis)
    }

    fun onLectureWeeksChange(weeks: String) {
        lectureWeeks = weeks
    }


    val canSave: Boolean
        get() {
            val start = startEpochDay
            val end = endEpochDay
            return start != null && end != null && end > start && (hoursPerCredit.isEmpty() || (hoursPerCredit.toIntOrNull()
                ?: 0) > 0)
        }

    fun save(
        onDone: () -> Unit
    ) {
        val original = loadedSemester
        val start = startEpochDay
        val end = endEpochDay
        if (start == null || end == null)
            return
        val hours = hoursPerCredit.toIntOrNull() ?: 30
        val weeks = lectureWeeks.toIntOrNull() ?: 14

        val semester =
            if (original != null) {
                original.copy(
                    startEpochDay = start,
                    endEpochDay = end,
                    hoursPerCredit = hours,
                    lectureWeeks = weeks
                )
            } else {
                Semester(
                    startEpochDay = start,
                    endEpochDay = end,
                    hoursPerCredit = hours,
                    lectureWeeks = weeks
                )
            }
        viewModelScope.launch {
            if (original != null) {
                semesterDao.update(semester)
            } else {
                semesterDao.insert(semester)
            }
            updateAllWidgets(getApplication())
            onDone()
        }
    }

    fun load() {
        if (loaded)
            return
        loaded = true

        viewModelScope.launch {
            val semester = semesterDao.getCurrentSemester().first()
            if (semester != null) {
                startEpochDay = semester.startEpochDay
                endEpochDay = semester.endEpochDay
                hoursPerCredit = semester.hoursPerCredit.toString()
                lectureWeeks = semester.lectureWeeks.toString()
                loadedSemester = semester
            }
        }
    }
}


