package com.tom.romcom.ui.course

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.tom.romcom.data.CredoDatabase
import com.tom.romcom.ui.courseColors

class CourseSetupViewModel(application: Application) : AndroidViewModel(application) {

    private val courseDao = CredoDatabase.getDatabase(context = application).courseDao()

    var name by mutableStateOf("")
        private set

    var credits by mutableStateOf("")
        private set

    var color by mutableLongStateOf(courseColors.first())
        private set

    fun onNameChange(courseName: String) {
        name = courseName
    }

    fun onCreditsChange(courseCredits: String) {
        credits = courseCredits
    }

    fun onColorChange(courseColor: Long) {
        color = courseColor
    }

}