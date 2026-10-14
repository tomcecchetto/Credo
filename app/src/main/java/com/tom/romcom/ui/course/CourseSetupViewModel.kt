package com.tom.romcom.ui.course

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tom.romcom.data.Course
import com.tom.romcom.data.CredoDatabase
import com.tom.romcom.data.Exam
import com.tom.romcom.ui.courseColors
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class CourseSetupViewModel(application: Application) : AndroidViewModel(application) {

    private val courseDao = CredoDatabase.getDatabase(context = application).courseDao()
    private val semesterDao = CredoDatabase.getDatabase(context = application).semesterDao()
    private val examDao = CredoDatabase.getDatabase(context = application).examDao()

    var name by mutableStateOf("")
        private set

    var credits by mutableStateOf("")
        private set

    var color by mutableLongStateOf(courseColors.first())
        private set

    var examEpochDay by mutableStateOf<Long?>(null)
        private set

    private var semesterId: Int? = null

    private val creditsValue: Double? get() = credits.replace(',', '.').toDoubleOrNull()




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

    fun onExamDateSelected(millis: Long) {
        examEpochDay = TimeUnit.MILLISECONDS.toDays(millis)
    }

    val canSave: Boolean
        get() {
            return name.isNotBlank() && (creditsValue ?: 0.0) > 0.0
        }

    fun save(
        onDone: () -> Unit
    ) {
        val semId = semesterId
        val creditsNum = creditsValue
        val examDate = examEpochDay

        if (name.isBlank() || creditsNum == null || semId == null)
            return

        val course = Course(
            name = name,
            credits = creditsNum,
            color = color,
            semesterId = semId
        )
        viewModelScope.launch {
            val newId = courseDao.insert(course).toInt()
            if (examDate != null) {
                examDao.insert(Exam(courseId = newId, epochDay = examDate))
            }
            onDone()
        }
    }
}