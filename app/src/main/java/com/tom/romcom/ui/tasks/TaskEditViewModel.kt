package com.tom.romcom.ui.tasks

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.tom.romcom.data.TaskType
import java.time.LocalDate

class TaskEditViewModel(application: Application) : AndroidViewModel(application){

    var taskTitle by mutableStateOf(TaskType.HOMEWORK)
        private set
    var taskType by mutableStateOf("")
        private set
    var courseId by mutableStateOf<Int?>(null)
        private set
    var dueDate by mutableLongStateOf(LocalDate.now().plusDays(1).toEpochDay())
        private set
    var dueTime by mutableStateOf<Int?>(null)
        private set
    var notes by mutableStateOf(null)
        private set
    var timeSet by mutableStateOf(false)
        private set
}