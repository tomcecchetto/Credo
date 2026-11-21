package com.tom.romcom.ui.tasks

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tom.romcom.data.CredoDatabase
import com.tom.romcom.data.ExamWithCourse
import com.tom.romcom.data.Task
import com.tom.romcom.data.TaskType
import com.tom.romcom.data.TaskWithCourse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.LocalDate

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val db = CredoDatabase.getDatabase(application)
    private val taskDao = db.taskDao()
    private val examDao = db.examDao()
    private val today = LocalDate.now().toEpochDay()
    val openTasks = taskDao.getOpenTasks()
    val completedTasks = taskDao.getCompletedTasks()
    val remainingExams: Flow<List<ExamWithCourse>> = examDao.getRemainingExams(today)
    private val _selectedTypes = MutableStateFlow<Set<TaskType>>(value = emptySet())
    val selectedTypes: StateFlow<Set<TaskType>> = _selectedTypes
    val openTasksByType: Flow<List<TaskWithCourse>> = combine(openTasks, selectedTypes) { tasks, types ->
        if (types.isEmpty()) tasks else tasks.filter { it.task.taskType in types }
    }
    val completedTasksByType: Flow<List<TaskWithCourse>> = combine(completedTasks, _selectedTypes) { tasks, types ->
        if (types.isEmpty()) tasks else tasks.filter { it.task.taskType in types }
    }

    fun setCompleted(task: Task, completed: Boolean) {
        val taskCopy = task.copy(isCompleted = completed)
        viewModelScope.launch {
            taskDao.update(taskCopy)
        }
    }
    fun deleteTasks(tasks: List<TaskWithCourse>) {
        val listOfTasks = tasks.map { it.task }
        viewModelScope.launch {
            taskDao.deleteTasks(listOfTasks)
        }
    }
    fun toggleType(taskType: TaskType) {
        _selectedTypes.value = if (taskType in _selectedTypes.value) _selectedTypes.value - taskType
            else _selectedTypes.value + taskType
    }

}