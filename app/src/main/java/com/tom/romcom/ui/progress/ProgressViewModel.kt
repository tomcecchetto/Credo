package com.tom.romcom.ui.progress

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tom.romcom.data.Course
import com.tom.romcom.data.CredoDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ProgressViewModel(application: Application) : AndroidViewModel(application) {
    private val courseDao = CredoDatabase.getDatabase(application).courseDao()

    val courses: Flow<List<Course>> = courseDao.getCoursesForSemester(1) //TODO: current semester (Phase 1)

    fun addCourse(course: Course) {
        viewModelScope.launch { courseDao.insert(course) }
    }
}