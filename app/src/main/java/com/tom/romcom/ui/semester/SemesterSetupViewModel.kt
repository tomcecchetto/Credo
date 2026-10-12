package com.tom.romcom.ui.semester

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.tom.romcom.data.CredoDatabase
import java.util.concurrent.TimeUnit


class SemesterSetupViewModel(application: Application) : AndroidViewModel(application) {

    private val semesterDao = CredoDatabase.getDatabase(application).semesterDao()
    var hoursPerCredit by mutableStateOf("")
        private set
    var countLectureHours by mutableStateOf(false)
        private set
    var startEpochDay by mutableStateOf<Long?>(null)
        private set
    var endEpochDay by mutableStateOf<Long?>(null)
        private set

    fun onHoursChange(hours: String) {
        hoursPerCredit = hours
    }

    fun onCountLectureHoursChange(state: Boolean) {
        countLectureHours = state
    }

    fun onStartDateSelected(millis: Long) {
        startEpochDay = TimeUnit.MILLISECONDS.toDays(millis)
    }

    fun onEndDateSelected(millis: Long) {
        endEpochDay = TimeUnit.MILLISECONDS.toDays(millis)
    }
}
