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
import com.tom.romcom.ui.formatMinuteOfDay
import kotlinx.coroutines.flow.first
import org.w3c.dom.Text
import java.time.LocalDate
import java.time.LocalTime

class TodayWidget : GlanceAppWidget() {

    override val sizeMode = SizeMode.Exact

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val db = CredoDatabase.getDatabase(context)
        val lectureDao = db.lectureDao()

        val today = LocalDate.now().toEpochDay()
        val lecturesToday = lectureDao.getLecturesForDay(today).first()
        val nowMinute = LocalTime.now().hour * 60 + LocalTime.now().minute
        provideContent {
            GlanceTheme {

                val remaining = lecturesToday.filter { it.endMinute > nowMinute }

                when {
                    lecturesToday.isEmpty() -> Box(
                        modifier = GlanceModifier
                            .fillMaxSize()
                            .background(GlanceTheme.colors.widgetBackground)
                            .clickable(actionStartActivity<MainActivity>()),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = context.getString(R.string.no_lectures_today),
                            style = TextStyle(
                                color = GlanceTheme.colors.onSurface,
                                textAlign = TextAlign.Center
                            )
                        )
                    }

                    remaining.isEmpty() -> Box(
                        modifier = GlanceModifier
                            .fillMaxSize()
                            .background(GlanceTheme.colors.widgetBackground)
                            .clickable(actionStartActivity<MainActivity>()),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = context.getString(R.string.lectures_complete),
                            style = TextStyle(
                                color = GlanceTheme.colors.onSurface,
                                textAlign = TextAlign.Center
                            )
                        )
                    }

                    else -> {
                        Column(
                            modifier = GlanceModifier
                                .fillMaxSize()
                                .background(GlanceTheme.colors.widgetBackground)
                                .padding(16.dp)
                                .clickable(actionStartActivity<MainActivity>())
                        ) {
                            Text(
                                text = context.getString(R.string.today),
                                style = TextStyle(
                                    color = GlanceTheme.colors.onSurface,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            )
                            LazyColumn(
                                modifier = GlanceModifier.defaultWeight()
                            ) {
                                items(remaining) { lecture ->
                                    Row(
                                        modifier = GlanceModifier
                                            .fillMaxWidth()
                                            .padding(top = 10.dp)
                                            .clickable(actionStartActivity<MainActivity>()),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(
                                            modifier = GlanceModifier
                                                .width(44.dp)
                                        ) {
                                            Text(
                                                text = formatMinuteOfDay(lecture.startMinute),
                                                style = TextStyle(
                                                    color = GlanceTheme.colors.onSurface,
                                                    fontSize = 14.sp
                                                )
                                            )
                                            Text(
                                                text = formatMinuteOfDay(lecture.endMinute),
                                                style = TextStyle(
                                                    color = GlanceTheme.colors.onSurfaceVariant,
                                                    fontSize = 12.sp)
                                            )
                                        }
                                        Box(
                                            modifier = GlanceModifier
                                                .width(4.dp)
                                                .height(36.dp)
                                                .cornerRadius(2.dp)
                                                .background(Color(lecture.color))
                                        ) {}
                                        Column(
                                            modifier = GlanceModifier
                                                .defaultWeight()
                                                .padding(start = 8.dp)
                                        ) {
                                            Text(
                                                text = lecture.courseName,
                                                style = TextStyle(
                                                    color = GlanceTheme.colors.onSurface,
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.Medium
                                                ),
                                                maxLines = 1
                                            )
                                            Text(
                                                text = if (lecture.profName.isNotBlank()) "${lecture.room} · ${lecture.profName}" else lecture.room,
                                                style = TextStyle(
                                                    color = GlanceTheme.colors.onSurfaceVariant,
                                                    fontSize = 12.sp
                                                ),
                                                maxLines = 1
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

}

class TodayWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = TodayWidget()
}
