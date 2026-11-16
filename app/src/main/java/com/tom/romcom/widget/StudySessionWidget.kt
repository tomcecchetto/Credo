package com.tom.romcom.widget

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import com.tom.romcom.MainActivity
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
                Box(
                    modifier = GlanceModifier
                        .fillMaxSize()
                        .background(GlanceTheme.colors.widgetBackground)
                        .padding(16.dp)
                        .clickable(actionStartActivity<MainActivity>())
                ) {
                    Column(
                        modifier = GlanceModifier
                            .fillMaxSize()
                    ) {
                        Row(
                            modifier = GlanceModifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = context.getString(R.string.app_name),
                                style = TextStyle(
                                    color = GlanceTheme.colors.onSurface,
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = GlanceModifier.defaultWeight()
                            )
                            Text(
                                text = context.getString(R.string.study_session),
                                style = TextStyle(
                                    color = GlanceTheme.colors.onSurfaceVariant,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 12.sp
                                )
                            )
                        }
                        if (courses.isEmpty()) {
                            Box(
                                modifier = GlanceModifier
                                    .fillMaxWidth()
                                    .defaultWeight(),
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
                            LazyColumn(
                                modifier = GlanceModifier
                                    .padding(top = 8.dp)
                                    .defaultWeight()
                            ) {
                                items(courses, itemId = { it.id.toLong() }) { course ->
                                    Row(
                                        modifier = GlanceModifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                            .clickable(onClick = actionStartActivity<MainActivity>()),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = GlanceModifier
                                                .width(4.dp)
                                                .height(32.dp)
                                                .cornerRadius(2.dp)
                                                .background(Color(course.color))
                                        ) { }
                                        Text(
                                            text = course.name,
                                            style = TextStyle(
                                                color = GlanceTheme.colors.onSurface,
                                                fontWeight = FontWeight.Medium
                                            ),
                                            maxLines = 1,
                                            modifier = GlanceModifier
                                                .defaultWeight()
                                                .padding(start = 12.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

class StudySessionWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = StudySessionWidget()
}

