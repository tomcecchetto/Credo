package com.tom.romcom.ui.progress

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tom.romcom.data.Course
import com.tom.romcom.data.CourseClassHours
import com.tom.romcom.data.CredoDatabase
import com.tom.romcom.data.Exam
import com.tom.romcom.data.Semester
import com.tom.romcom.data.StudySession
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ProgressViewModel(application: Application) : AndroidViewModel(application) {

    private val db = CredoDatabase.getDatabase(application)
    private val courseDao = db.courseDao()
    private val semesterDao = db.semesterDao()
    private val examDao = db.examDao()
    private val studySessionDao = db.studySessionDao()
    private val lectureDao = db.lectureDao()

    val semester: Flow<Semester?> = semesterDao.getCurrentSemester()

    val finishedSessions: Flow<List<StudySession>> = semester.flatMapLatest { current ->
        if (current == null) flowOf(emptyList())
        else studySessionDao.getFinishedSessionsForSemester(current.id)
    }
    val exams: Flow<List<Exam>> = semester.flatMapLatest { current ->
        if (current == null) flowOf(emptyList())
        else examDao.getExamsForSemester(current.id)
    }
    val courses: Flow<List<Course>> = semester.flatMapLatest { current ->
        if (current == null) flowOf(emptyList())
        else courseDao.getCoursesForSemester(current.id)
    }
    val classHoursPerCourse: Flow<List<CourseClassHours>> = lectureDao.getClassHoursPerCourse()

    fun addCourse(course: Course) {
        viewModelScope.launch { courseDao.insert(course) }
    }
}