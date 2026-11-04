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
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalCoroutinesApi::class)
class SessionEditViewModel(application: Application) : AndroidViewModel(application) {

    private val db = CredoDatabase.getDatabase(application)
    private val sessionDao = db.studySessionDao()
    private val semesterDao = db.semesterDao()
    private val courseDao = db.courseDao()
    private var loaded = false
    private var loadedSession: StudySession? = null

    private fun toMillis(day: Long, minute: Int): Long =
        LocalDate.ofEpochDay(day)
            .atTime(LocalTime.of(minute / 60, minute % 60))
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

    var courseId by mutableStateOf<Int?>(null)
        private set
    var epochDay by mutableStateOf<Long?>(null)
        private set
    var startMinute by mutableStateOf<Int?>(null)
        private set
    var endMinute by mutableStateOf<Int?>(null)
        private set

    val semester: Flow<Semester?> = semesterDao.getCurrentSemester()

    val courses: Flow<List<Course>> = semester.flatMapLatest { current ->
        if (current == null) flowOf(emptyList())
        else courseDao.getCoursesForSemester(current.id)
    }

    fun onCourseSelected(id: Int) {
        courseId = id
    }

    fun onDateSelected(millis: Long) {
        epochDay = TimeUnit.MILLISECONDS.toDays(millis)
    }

    fun onStartTimeSelected(minute: Int) {
        startMinute = minute
    }

    fun onEndTimeSelected(minute: Int) {
        endMinute = minute
    }

    val canSave: Boolean
        get() {
            val cId = courseId
            val day = epochDay
            val start = startMinute
            val end = endMinute
            return start != null && end != null && end != start && day != null && cId != null
        }

    fun load(sessionId: Int?) {

        if (loaded)
            return
        loaded = true

        if (sessionId != null) {
            viewModelScope.launch {
                val session = sessionDao.getSessionById(sessionId)
                if (session != null) {
                    courseId = session.courseId
                    loadedSession = session
                    val start =
                        Instant.ofEpochMilli(session.startTime).atZone(ZoneId.systemDefault())
                    epochDay = start.toLocalDate().toEpochDay()
                    startMinute = start.hour * 60 + start.minute
                    if (session.endTime != null) {
                        val end =
                            Instant.ofEpochMilli(session.endTime).atZone(ZoneId.systemDefault())
                        endMinute = end.hour * 60 + end.minute
                    }
                }
            }
        }
        else epochDay = LocalDate.now().toEpochDay()
    }
    fun save(onDone: () -> Unit) {
        val cId = courseId
        val day = epochDay
        val startMin = startMinute
        val endMin = endMinute
        val loadedSess = loadedSession

        if (cId == null || day == null || startMin == null || endMin == null || endMin == startMin) return
        val endDay = if (endMin > startMin) day else day + 1


        val startMillis = toMillis(day, startMin)
        val endMillis = toMillis(endDay, endMin)

        val session =
            if (loadedSess != null) {
                loadedSess.copy(
                    courseId = cId,
                    startTime = startMillis,
                    endTime = endMillis
                )
            } else {
                StudySession(
                    courseId = cId,
                    startTime = startMillis,
                    endTime = endMillis
                )
            }
        viewModelScope.launch {
            if (loadedSess != null) sessionDao.update(session)
            else sessionDao.insert(session)
            updateAllWidgets(getApplication())
            onDone()
        }

    }
    fun delete(onDone: () -> Unit) {
        val loadedSess = loadedSession ?: return
        viewModelScope.launch {
            sessionDao.delete(loadedSess)
            updateAllWidgets(getApplication())
            onDone()
        }
    }
}


