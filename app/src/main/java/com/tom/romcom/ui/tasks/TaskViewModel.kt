package com.tom.romcom.ui.tasks

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tom.romcom.data.CredoDatabase
import com.tom.romcom.data.Task
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val db = CredoDatabase.getDatabase(application)
    private val taskDao = db.taskDao()
    val openTasks = taskDao.getOpenTasks()
    val completedTasks = taskDao.getCompletedTasks()

    fun setCompleted(task: Task, completed: Boolean) {
        val taskCopy = task.copy(isCompleted = completed)
        viewModelScope.launch {
            taskDao.update(taskCopy)
        }
    }
}