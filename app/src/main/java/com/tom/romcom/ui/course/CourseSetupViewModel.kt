package com.tom.romcom.ui.course

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tom.romcom.data.CredoDatabase
import com.tom.romcom.ui.courseColors
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CourseSetupViewModel(application: Application) : AndroidViewModel(application) {

    private val courseDao = CredoDatabase.getDatabase(context = application).courseDao()
    private val semesterDao = CredoDatabase.getDatabase(context = application).semesterDao()

    var name by mutableStateOf("")
        private set

    var credits by mutableStateOf("")
        private set

    var color by mutableLongStateOf(courseColors.first())
        private set

    private var semesterId: Int? = null

    init {
        viewModelScope.launch {
            semesterId = semesterDao.getCurrentSemester().first()?.id
            val semId = semesterId
            if (semId != null) {
                val courses = courseDao.getCoursesForSemester(semId).first()
                val taken = courses.map { it.color }
                color = courseColors.firstOrNull { it !in taken } ?: courseColors.first()
            }
        }
    }

    fun onNameChange(courseName: String) {
        name = courseName
    }

    fun onCreditsChange(courseCredits: String) {
        credits = courseCredits
    }

    fun onColorChange(courseColor: Long) {
        color = courseColor
    }

}