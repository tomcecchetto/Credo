package com.tom.romcom.data

import androidx.compose.ui.res.stringResource
import androidx.room.withTransaction
import com.tom.romcom.R
import kotlinx.serialization.json.Json
import java.time.LocalDate

class BackupManager(private val db: CredoDatabase) {

    private val json = Json { encodeDefaults = true; prettyPrint = true; ignoreUnknownKeys = true }

    suspend fun buildBackupFile(): BackupFile {
        return BackupFile(
            dbVersion = CredoDatabase.DATABASE_VERSION,
            exportEpochDay = LocalDate.now().toEpochDay(),
            semesters = db.semesterDao().getAllSemestersOnce(),
            courses = db.courseDao().getAllCoursesOnce(),
            recurringSlots = db.recurringSlotDao().getAllSlotsOnce(),
            lectures = db.lectureDao().getAllLecturesOnce(),
            exams = db.examDao().getAllExamsOnce(),
            studySessions = db.studySessionDao().getAllStudySessionsOnce(),
            tasks = db.taskDao().getAllTasksOnce()
        )
    }

    fun toJson(backupFile: BackupFile): String {
        return json.encodeToString(backupFile)
    }

    fun fromJson(text: String): BackupFile {
        return json.decodeFromString<BackupFile>(text)
    }

    suspend fun restore(backup: BackupFile) {

        if (backup.dbVersion > CredoDatabase.DATABASE_VERSION) error("Backup is from a newer app version")

        db.withTransaction {
            db.taskDao().deleteAllTasks()
            db.studySessionDao().deleteAllStudySessions()
            db.examDao().deleteAllExams()
            db.lectureDao().deleteAllLectures()
            db.recurringSlotDao().deleteAllSlots()
            db.courseDao().deleteAllCourses()
            db.semesterDao().deleteAllSemesters()

            db.semesterDao().insertSemesters(backup.semesters)
            db.courseDao().insertCourses(backup.courses)
            db.recurringSlotDao().insertSlots(backup.recurringSlots)
            db.lectureDao().insertLectures(backup.lectures)
            db.examDao().insertExams(backup.exams)
            db.studySessionDao().insertStudySessions(backup.studySessions)
            db.taskDao().insertTasks(backup.tasks)
        }
    }
}
