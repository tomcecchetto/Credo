package com.tom.romcom.widget

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import com.tom.romcom.R
import com.tom.romcom.data.CredoDatabase
import kotlinx.coroutines.flow.first

class StudySessionWidget : GlanceAppWidget() {

    override val sizeMode = SizeMode.Exact
    override suspend fun provideGlance(context: Context, id: GlanceId) {

        val db = CredoDatabase.getDatabase(context)
        val semesterDao = db.semesterDao()
        val courseDao = db.courseDao()

        val semester = semesterDao.getCurrentSemesterOnce() ?: provideContent {
            GlanceTheme {
                Box(
                    modifier = GlanceModifier
                        .fillMaxSize()
                        .background(GlanceTheme.colors.widgetBackground),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = context.getString(R.string.add_semester_widget),
                        style = TextStyle(
                            color = GlanceTheme.colors.onSurface,
                            textAlign = TextAlign.Center
                        )
                    )
                }
            }
        }
        val courses = courseDao.getCoursesForSemester(semester.id).first()
        provideContent {
            GlanceTheme {
                if (courses.isEmpty()) {
                    Box(
                        modifier = GlanceModifier
                            .fillMaxSize()
                            .background(colorProvider = GlanceTheme.colors.widgetBackground),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = context.getString(R.string.add_course_first),
                            style = TextStyle(
                                color = GlanceTheme.colors.onSurface,
                                textAlign = TextAlign.Center
                            )
                        )
                    }
                } else {

                }
            }
        }
    }
}