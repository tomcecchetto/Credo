package com.tom.romcom.ui.progress

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tom.romcom.R
import com.tom.romcom.data.BackupManager
import com.tom.romcom.data.Course
import com.tom.romcom.data.CourseClassHours
import com.tom.romcom.data.CredoDatabase
import com.tom.romcom.data.Exam
import com.tom.romcom.data.Semester
import com.tom.romcom.data.StudySession
import com.tom.romcom.widget.updateAllWidgets
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    var backupMessage by mutableStateOf<String?>(null)
        private set

    fun clearBackupMessage() {
        backupMessage = null
    }

    fun addCourse(course: Course) {
        viewModelScope.launch { courseDao.insert(course) }
    }

    fun exportBackup(uri: Uri) {
        viewModelScope.launch {
            try {
                val manager = BackupManager(db)
                val backup = manager.buildBackupFile()
                val json = manager.toJson(backup)
                withContext(Dispatchers.IO) {
                    getApplication<Application>().contentResolver.openOutputStream(uri)
                        ?.use { it.write(json.toByteArray()) }
                } ?: error("Could not write file")
                backupMessage = getApplication<Application>().getString(R.string.backup_saved)
            } catch (e: Exception) {
                Log.e("Credo", "Export failed", e)
                backupMessage = getApplication<Application>().getString(R.string.backup_failed)
            }
        }
    }

    fun importBackup(uri: Uri) {
        viewModelScope.launch {
            try {
                val manager = BackupManager(db)
                val text = withContext(Dispatchers.IO) {
                    getApplication<Application>().contentResolver.openInputStream(uri)
                        ?.bufferedReader()?.use { it.readText() }
                } ?: error("Could not read file")
                val backup = manager.fromJson(text)
                manager.restore(backup)
                updateAllWidgets(getApplication())
                backupMessage = getApplication<Application>().getString(R.string.backup_imported)
            } catch (e: Exception) {
                Log.e("Credo", "Backup import failed", e)
                backupMessage = getApplication<Application>().getString(R.string.backup_import_failed)
            }
        }
    }
}
