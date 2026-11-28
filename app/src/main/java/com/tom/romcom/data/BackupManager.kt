package com.tom.romcom.data

import kotlinx.serialization.json.Json
import java.time.LocalDate

class BackupManager(private val db: CredoDatabase) {

    private val json = Json { encodeDefaults = true; prettyPrint = true }

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
}
