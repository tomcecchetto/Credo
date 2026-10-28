package com.tom.romcom.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import androidx.glance.color.ColorProvider
import com.tom.romcom.R
import com.tom.romcom.data.CredoDatabase
import com.tom.romcom.ui.formatHours
import com.tom.romcom.ui.formatHoursDiff
import com.tom.romcom.ui.progress.planProgressFor
import com.tom.romcom.ui.theme.Amber
import com.tom.romcom.ui.theme.Green
import kotlinx.coroutines.flow.first
import kotlin.math.abs

class CredoWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val db = CredoDatabase.getDatabase(context)
        val semesterDao = db.semesterDao()
        val courseDao = db.courseDao()
        val studySessionDao = db.studySessionDao()

        val semester = semesterDao.getCurrentSemester().first() ?: provideContent {
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
                        ),
                    )
                }
            }
        }

        val courses = courseDao.getCoursesForSemester(semester.id).first()
        val sessions = studySessionDao.getFinishedSessionsForSemester(semester.id).first()
        val coursesWithPlannedHours = courses.filter { it.plannedHoursPerWeek > 0 }
        val rows = coursesWithPlannedHours.map { it to planProgressFor(course = it, semester = semester, sessions = sessions) }


        provideContent {
            GlanceTheme {
                Box(
                    modifier = GlanceModifier
                        .fillMaxSize()
                        .background(GlanceTheme.colors.widgetBackground)
                        .padding(16.dp)
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
                                    fontWeight = FontWeight.Bold),
                                modifier = GlanceModifier.defaultWeight()
                            )
                            Text(
                                text = context.getString(R.string.planned),
                                style = TextStyle(
                                    color = GlanceTheme.colors.onSurfaceVariant,
                                    fontSize = 12.sp
                                )
                            )
                        }
                        for ((course, progress) in rows) {
                            val diff = progress.hoursDone - progress.planExpectedHours
                            Spacer(GlanceModifier.height(10.dp))
                            Row(
                                modifier = GlanceModifier
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = GlanceModifier
                                        .width(4.dp)
                                        .height(36.dp)
                                        .cornerRadius(2.dp)
                                        .background(Color(course.color))
                                ) {}
                                Column(
                                    modifier = GlanceModifier
                                        .defaultWeight()
                                        .padding(start = 12.dp)
                                ) {
                                    Row(
                                        modifier = GlanceModifier
                                            .fillMaxWidth()
                                    ) {
                                        Text(
                                            text = course.name,
                                            style = TextStyle(
                                                color = GlanceTheme.colors.onSurface,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Medium
                                            ),
                                            maxLines = 1,
                                            modifier = GlanceModifier.defaultWeight()
                                        )
                                        Text(
                                            text = context.getString(R.string.hours_diff, formatHoursDiff(diff = diff)),
                                            style = TextStyle(
                                                color = if (diff >= 0.0) ColorProvider(Green, Green) else ColorProvider(Amber, Amber)
                                            )
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

class CredoWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = CredoWidget()
}

@Composable
private fun formatDiffTextWidget(
    diff: Double,
    context: Context
): Pair<String, String> {
    return if (diff >= 0) "" to context.getString(R.string.hours_diff, formatHoursDiff(diff))
    else context.getString(R.string.to_go) to context.getString(R.string.hours_diff, formatHours(abs(diff)))
}
