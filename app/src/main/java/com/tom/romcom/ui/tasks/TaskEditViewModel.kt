package com.tom.romcom.ui.tasks

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tom.romcom.data.Course
import com.tom.romcom.data.CredoDatabase
import com.tom.romcom.data.Semester
import com.tom.romcom.data.Task
import com.tom.romcom.data.TaskType
import com.tom.romcom.ui.nextFreeColor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalCoroutinesApi::class)
class TaskEditViewModel(application: Application) : AndroidViewModel(application){

    private val db = CredoDatabase.getDatabase(application)
    private val semesterDao = db.semesterDao()
    private val courseDao = db.courseDao()
    private val taskDao = db.taskDao()

    var taskTitle by mutableStateOf("")
        private set
    var taskType by mutableStateOf(TaskType.HOMEWORK)
        private set
    var courseId by mutableStateOf<Int?>(null)
        private set
    var dueDate by mutableLongStateOf(LocalDate.now().plusDays(1).toEpochDay())
        private set
    var dueTime by mutableStateOf<Int?>(null)
        private set
    var notes by mutableStateOf<String?>(null)
        private set
    var timeSet by mutableStateOf(false)
        private set
    val semester: Flow<Semester?> = semesterDao.getCurrentSemester()
    val courses: Flow<List<Course>> = semester.flatMapLatest { current ->
        if (current == null) flowOf(emptyList())
        else courseDao.getCoursesForSemester(current.id)
    }
    private var loaded = false
    private var loadedTask: Task? = null

    fun onTaskTitleChange(title: String) {
        taskTitle = title
    }
    fun onTaskTypeChange(newTaskType: TaskType) {
        taskType = newTaskType
    }
    fun onCourseIdChange(id: Int?) {
        courseId = id
    }
    fun onDueDateChange(millis: Long) {
        dueDate = TimeUnit.MILLISECONDS.toDays(millis)
    }
    fun onDueTimeChange(time: Int) {
        dueTime = time
    }
    fun onNotesChange(note: String) {
        notes = note
    }
    fun onTimeSetChange(state: Boolean) {
        timeSet = state
    }

    fun load(taskId: Int?) {

        if (loaded)
            return
        loaded = true

        if (taskId != null) {
            viewModelScope.launch {
                val task = taskDao.getTaskById(taskId)
                if (task != null) {
                    loadedTask = task
                    taskTitle = task.title
                    taskType = task.taskType
                    courseId = task.courseId
                    dueDate = task.dueEpochDay
                    notes = task.notes
                    dueTime = task.dueMinuteOfDay
                    timeSet = task.dueMinuteOfDay != null
                }
            }
        }
    }
    fun save(onDone: () -> Unit) {

        val existing = loadedTask
        if (taskTitle.isBlank() || (timeSet && dueTime == null)) return
        val dueMinute = if (timeSet) dueTime else null
        val cleanNotes = if (notes.isNullOrBlank()) null else notes
        val task =
            if (existing != null) {
                existing.copy(
                    title = taskTitle,
                    taskType = taskType,
                    courseId = courseId,
                    dueEpochDay = dueDate,
                    notes = cleanNotes,
                    dueMinuteOfDay = dueMinute,
                )
            } else {
                Task(
                    title = taskTitle,
                    taskType = taskType,
                    courseId = courseId,
                    dueEpochDay = dueDate,
                    notes = cleanNotes,
                    dueMinuteOfDay = dueMinute
                )
            }
        viewModelScope.launch {
            if (existing != null) taskDao.update(task)
            else taskDao.insert(task)
            onDone()
        }
    }
    fun delete(onDone: () -> Unit) {
        val existing = loadedTask ?: return
        viewModelScope.launch {
            taskDao.delete(existing)
            onDone()
        }
    }
    fun createCourse(
        name: String,
        credits: Double
    ) {
        viewModelScope.launch {
            val currentSemester = semester.first() ?: return@launch
            val color = nextFreeColor(courses.first())
            val newId = courseDao.insert(
                Course(
                    name = name,
                    credits = credits,
                    color = color,
                    semesterId = currentSemester.id
                )
            ).toInt()
            courseId = newId
        }
    }
}
