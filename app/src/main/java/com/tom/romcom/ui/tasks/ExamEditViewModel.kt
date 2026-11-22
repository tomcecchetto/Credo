package com.tom.romcom.ui.tasks

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tom.romcom.data.CredoDatabase
import com.tom.romcom.data.ExamWithCourse
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.concurrent.TimeUnit

class ExamEditViewModel(application: Application) : AndroidViewModel(application) {

    private val db = CredoDatabase.getDatabase(application)
    private val examDao = db.examDao()

    var epochDay by mutableLongStateOf(LocalDate.now().plusDays(1).toEpochDay())
        private set
    var startMinuteOfDay by mutableStateOf<Int?>(null)
        private set
    var endMinuteOfDay by mutableStateOf<Int?>(null)
        private set
    var room by mutableStateOf("")
        private set
    var courseName by mutableStateOf("")
        private set
    private var loaded = false
    private var loadedExamWithCourse: ExamWithCourse? = null

    fun onEpochDayChange(millis: Long) {
        epochDay = TimeUnit.MILLISECONDS.toDays(millis)
    }
    fun onStartMinuteOfDayChange(startMinute: Int) {
        startMinuteOfDay = startMinute
    }
    fun onEndMinuteOfDayChange(endMinute: Int) {
        endMinuteOfDay = endMinute
    }
    fun onRoomChange(newRoom: String) {
        room = newRoom
    }
    fun onStartMinuteOfDayClear() {
        startMinuteOfDay = null
    }
    fun onEndMinuteOfDayClear() {
        endMinuteOfDay = null
    }

    fun load(examId: Int) {

        if (loaded)
            return
        loaded = true

        viewModelScope.launch {
            val examWithCourse = examDao.getExamById(examId)
            if (examWithCourse != null) {
                loadedExamWithCourse = examWithCourse
                epochDay = examWithCourse.exam.epochDay
                startMinuteOfDay = examWithCourse.exam.startMinuteOfDay
                endMinuteOfDay = examWithCourse.exam.endMinuteOfDay
                room = examWithCourse.exam.room ?: ""
                courseName = examWithCourse.courseName
            }
        }
    }
    val canSave: Boolean
        get() {
            val start = startMinuteOfDay
            val end = endMinuteOfDay
            return !(start != null && end != null && start > end)
        }
    fun save(onDone: () -> Unit) {

        val start = startMinuteOfDay
        val end = endMinuteOfDay
        val cleanRoom = if (room.isBlank()) null else room
        val existing = loadedExamWithCourse ?: return
        if (start != null && end != null && start > end) return

        val exam = existing.exam.copy(
            startMinuteOfDay = start,
            endMinuteOfDay = end,
            epochDay = epochDay,
            room = cleanRoom
        )
        viewModelScope.launch {
            examDao.update(exam)
            onDone()
        }
    }
    fun delete(onDone: () -> Unit) {
        val existing = loadedExamWithCourse ?: return
        val exam = existing.exam
        viewModelScope.launch {
            examDao.delete(exam)
            onDone()
        }
    }
}
