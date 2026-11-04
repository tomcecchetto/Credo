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
import com.tom.romcom.ui.nextFreeColor
import com.tom.romcom.widget.updateAllWidgets
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

    var praktikumHoursPerSession by mutableStateOf("")
        private set

    var praktikumSessions by mutableStateOf("")
        private set

    var plannedHoursPerWeek by mutableStateOf("")
        private set

    var profName by mutableStateOf("")
        private set



    private var semesterId: Int? = null

    private var editingCourseId: Int? = null

    private var loadedExam: Exam? = null

    private var loaded = false

    private val creditsValue: Double? get() = credits.replace(',', '.').toDoubleOrNull()

    private var loadedCourse: Course? = null

    private val lectureHoursValue: Double? get() = lectureHours.replace(',', '.').toDoubleOrNull()

    private val praktikumHoursValue: Double? get() = praktikumHoursPerSession.replace(',', '.').toDoubleOrNull()

    private val plannedHoursPerWeekValue: Double? get() = plannedHoursPerWeek.replace(',', '.').toDoubleOrNull()

    private val praktikumSessionsValue: Int? get() = praktikumSessions.toIntOrNull()



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

    fun onExamDateCleared() {
        examEpochDay = null
    }

    fun onLectureHoursChange(courseLectureHours: String) {
        lectureHours = courseLectureHours
    }

    fun onPraktikumHoursPerSessionChange(hoursPerSession: String) {
        praktikumHoursPerSession = hoursPerSession
    }

    fun onPraktikumSessionsChange(praktSession: String) {
        praktikumSessions = praktSession
    }

    fun onPlannedHoursPerWeekChange(hoursPerWeek: String) {
        plannedHoursPerWeek = hoursPerWeek
    }

    fun onProfNameChange(name: String) {
        profName = name
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
                    praktikumHoursPerSession = if (course.praktikumHoursPerSession == 0.0) "" else formatNumber(course.praktikumHoursPerSession)
                    praktikumSessions = if (course.praktikumSessions == 0) "" else course.praktikumSessions.toString()
                    plannedHoursPerWeek = if (course.plannedHoursPerWeek == 0.0) "" else formatNumber(course.plannedHoursPerWeek)
                    profName = course.profName
                }
            }
        } else {
            viewModelScope.launch {
                semesterId = semesterDao.getCurrentSemester().first()?.id
                val semId = semesterId
                if (semId != null) {
                    val courses = courseDao.getCoursesForSemester(semId).first()
                    color = nextFreeColor(courses)
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
        val praktHours = praktikumHoursValue ?: 0.0
        val plannedHours = plannedHoursPerWeekValue ?: 0.0
        val praktSess = praktikumSessionsValue ?: 0

        if (name.isBlank() || creditsNum == null || semId == null)
            return

        val course =
            if (original != null) {
                original.copy(
                    name = name,
                    credits = creditsNum,
                    color = color,
                    lectureHoursPerWeek = lectureHoursNum,
                    praktikumHoursPerSession = praktHours,
                    plannedHoursPerWeek = plannedHours,
                    praktikumSessions = praktSess,
                    profName = profName
                )
            } else {
                Course(
                    name = name,
                    credits = creditsNum,
                    color = color,
                    semesterId = semId,
                    lectureHoursPerWeek = lectureHoursNum,
                    praktikumHoursPerSession = praktHours,
                    plannedHoursPerWeek = plannedHours,
                    praktikumSessions = praktSess,
                    profName = profName
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
            } else if (exam != null) {
                examDao.delete(exam)
            }
            updateAllWidgets(getApplication())
            onDone()
        }
    }

    fun delete(onDone: () -> Unit) {
        val editId = editingCourseId ?: return
        viewModelScope.launch {
            courseDao.deleteById(editId)
            updateAllWidgets(getApplication())
            onDone()
        }
    }
}
