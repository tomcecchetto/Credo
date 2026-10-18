package com.tom.romcom.ui.semester

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tom.romcom.data.CredoDatabase
import com.tom.romcom.data.Semester
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


class SemesterSetupViewModel(application: Application) : AndroidViewModel(application) {

    private val semesterDao = CredoDatabase.getDatabase(application).semesterDao()
    var hoursPerCredit by mutableStateOf("")
        private set

    var startEpochDay by mutableStateOf<Long?>(null)
        private set
    var endEpochDay by mutableStateOf<Long?>(null)
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
        val start = startEpochDay
        val end = endEpochDay
        if (start == null || end == null)
            return
        val hours = hoursPerCredit.toIntOrNull() ?: 30

        val semester = Semester(
            startEpochDay = start,
            endEpochDay = end,
            hoursPerCredit = hours,
        )
        viewModelScope.launch {
            semesterDao.insert(semester)
            onDone()
        }
    }
}


