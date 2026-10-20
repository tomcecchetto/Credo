package com.tom.romcom.ui.course

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tom.romcom.R
import com.tom.romcom.data.Course
import com.tom.romcom.data.Exam
import com.tom.romcom.ui.formatNumber
import com.tom.romcom.ui.formatHoursDiff
import com.tom.romcom.ui.progress.HoursTrack
import com.tom.romcom.ui.progress.ProgressViewModel
import java.time.LocalDate

@Composable
fun CourseCard(
    totalHours: Double,
    planTotalHours: Double,
    expectedHours: Double,
    planExpectedHours: Double,
    hoursDone: Double,
    course: Course,
    onClick: () -> Unit,
    nextExam: Exam? = null,
    hasExam: Boolean,
    modifier: Modifier = Modifier,
    viewModel: ProgressViewModel = viewModel()

    ) {
    val exams by viewModel.exams.collectAsState(initial = emptyList())
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        onClick = onClick
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(Color(course.color), shape = CircleShape)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = course.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = stringResource(R.string.credits_value, formatNumber(course.credits)),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                val today = LocalDate.now().toEpochDay()
                if (nextExam != null) {
                    val daysLeft = (nextExam.epochDay - today).toInt()
                    Text(
                        text = if (daysLeft == 0) stringResource(R.string.exam_today) else pluralStringResource(
                            R.plurals.exam_in_days,
                            daysLeft,
                            daysLeft
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = if (daysLeft in 1..7)
                            MaterialTheme.colorScheme.error else
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = if (hasExam) stringResource(R.string.exam) else stringResource(R.string.semester),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.width(50.dp)
                    )
                    HoursTrack(
                        totalHours = totalHours,
                        expectedHours = expectedHours,
                        hoursDone = hoursDone,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = stringResource(
                            R.string.hours_diff,
                            formatHoursDiff(hoursDone - expectedHours)
                        ),
                        textAlign = TextAlign.End,
                        modifier = Modifier.width(60.dp)
                    )
                }
                if (course.plannedHoursPerWeek > 0.0) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.plan),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.width(44.dp)
                        )
                        HoursTrack(
                            totalHours = planTotalHours,
                            hoursDone = hoursDone,
                            expectedHours = planExpectedHours,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = stringResource(
                                R.string.hours_diff,
                                formatHoursDiff(hoursDone - planExpectedHours)
                            ),
                            textAlign = TextAlign.End,
                            modifier = Modifier.width(60.dp)
                        )
                    }
                }
            }
        }
    }
}
