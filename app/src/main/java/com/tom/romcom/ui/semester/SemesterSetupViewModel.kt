package com.tom.romcom.ui.semester

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.tom.romcom.data.CredoDatabase


class SemesterSetupViewModel(application: Application) : AndroidViewModel(application) {

    private val semesterDao = CredoDatabase.getDatabase(application).semesterDao()
    var hoursPerCredit by mutableStateOf("30")
        private set
    var countLectureHours by mutableStateOf(false)
        private set

    fun onHoursChange(hours: String){
        hoursPerCredit = hours
    }

    fun onCountLectureHoursChange(state: Boolean) {
         countLectureHours = state
    }
}
