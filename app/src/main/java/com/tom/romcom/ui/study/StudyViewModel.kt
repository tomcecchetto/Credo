package com.tom.romcom.ui.study

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tom.romcom.data.Course
import com.tom.romcom.data.CredoDatabase
import com.tom.romcom.data.Semester
import com.tom.romcom.data.StudySession
import com.tom.romcom.widget.updateAllWidgets
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class StudyViewModel(application: Application) : AndroidViewModel(application) {

    private val db = CredoDatabase.getDatabase(application)
    private val studySessionDao = db.studySessionDao()
    private val semesterDao = db.semesterDao()
    private val courseDao = db.courseDao()

    val semester: Flow<Semester?> = semesterDao.getCurrentSemester()

    var selectedCourseId by mutableStateOf<Int?>(null)
        private set
    val runningSession: Flow<StudySession?> = studySessionDao.getRunningSession()
    val finishedSessions: Flow<List<StudySession>> = semester.flatMapLatest { current ->
        if (current == null) flowOf(emptyList())
        else studySessionDao.getFinishedSessionsForSemester(current.id)
    }
    val courses: Flow<List<Course>> = semester.flatMapLatest { current ->
        if (current == null) flowOf(emptyList())
        else courseDao.getCoursesForSemester(current.id)
    }

    fun onCourseSelected(courseId: Int) {
        selectedCourseId = courseId
    }

    fun start() {
        val selectedId = selectedCourseId ?: return
        viewModelScope.launch {
            val courseExists = courses.first().any { it.id == selectedId }
            if (!courseExists) {
                selectedCourseId = null
                return@launch
            }
            studySessionDao.startIfNoneRunning(
                StudySession(
                    courseId = selectedId,
                    startTime = System.currentTimeMillis()
                )
            )
        }
    }

    fun stop() {
        viewModelScope.launch {
            val running = studySessionDao.getRunningSessionNow()
            if (running != null) {
                studySessionDao.update(
                    running.copy(endTime = System.currentTimeMillis())
                )
                updateAllWidgets(getApplication())
            }
        }
    }
}
