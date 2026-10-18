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
import com.tom.romcom.ui.formatNumber
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

    var lectureHours by mutableStateOf("")
        private set

    private var semesterId: Int? = null

    private var editingCourseId: Int? = null

    private var loadedExam: Exam? = null

    private var loaded = false

    private val creditsValue: Double? get() = credits.replace(',', '.').toDoubleOrNull()

    private var loadedCourse: Course? = null

    private val lectureHoursValue: Double? get() = lectureHours.replace(',', '.').toDoubleOrNull()


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

    fun onLectureHoursChange(courseLectureHours: String) {
        lectureHours = courseLectureHours
    }

    fun load(courseId: Int?) {

        if (loaded)
            return
        loaded = true

        if (courseId != null) {
            viewModelScope.launch {
                val course = courseDao.getCourse(courseId)
                if (course != null) {
                    name = course.name
                    credits = formatNumber(course.credits)
                    color = course.color
                    editingCourseId = courseId
                    semesterId = course.semesterId
                    loadedExam = examDao.getExamsForCourse(courseId).first().firstOrNull()
                    examEpochDay = loadedExam?.epochDay
                    loadedCourse = course
                    lectureHours = if (course.lectureHoursPerWeek > 0.0) formatNumber(course.lectureHoursPerWeek) else ""
                }
            }
        } else {
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
        val editId = editingCourseId
        val exam = loadedExam
        val original = loadedCourse
        val lectureHoursNum = lectureHoursValue ?: 0.0

        if (name.isBlank() || creditsNum == null || semId == null)
            return

        val course =
            if (original != null) {
                original.copy(
                    name = name,
                    credits = creditsNum,
                    color = color,
                    lectureHoursPerWeek = lectureHoursNum
                )
            } else {
                Course(
                    name = name,
                    credits = creditsNum,
                    color = color,
                    semesterId = semId,
                    lectureHoursPerWeek = lectureHoursNum
                )
            }
        viewModelScope.launch {
            val id = if (editId != null) {
                courseDao.update(course)
                editId
            } else courseDao.insert(course).toInt()

            if (examDate != null) {
                if (exam != null) {
                    examDao.update(exam.copy(epochDay = examDate))
                } else examDao.insert(Exam(courseId = id, epochDay = examDate))
            }
            onDone()
        }
    }

    fun delete(onDone: () -> Unit) {
        val editId = editingCourseId ?: return
        viewModelScope.launch {
            courseDao.deleteById(editId)
            onDone()
        }
    }
}
