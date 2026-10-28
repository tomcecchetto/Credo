package com.tom.romcom.ui.course

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.tom.romcom.R
import com.tom.romcom.data.Course
import com.tom.romcom.data.Exam
import com.tom.romcom.ui.formatHours
import com.tom.romcom.ui.formatHoursDiff
import com.tom.romcom.ui.formatNumber
import com.tom.romcom.ui.progress.HoursTrack
import com.tom.romcom.ui.theme.Amber
import com.tom.romcom.ui.theme.PurpleHeart
import java.time.LocalDate
import kotlin.math.abs

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
    goalReached: Boolean,
    modifier: Modifier = Modifier,

    ) {
    val hoursDiffWidth = 85.dp
    val textModifierWidth = 50.dp
    val numberStyle = MaterialTheme.typography.bodyMedium.copy(fontFeatureSettings = "tnum")

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
                .padding(12.dp)
                .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(Color(course.color), RoundedCornerShape(2.dp))
            )
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = course.name,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.weight(1f, fill = false),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (goalReached) Icon(painterResource(R.drawable.ic_heart),
                        contentDescription = stringResource(R.string.goal_reached),
                        modifier = Modifier.size(16.dp),
                        tint = PurpleHeart
                    )
                }
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
                            Amber else
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val (prefix, value) = formatDiffText(hoursDone - expectedHours)
                    Text(
                        text = if (hasExam) stringResource(R.string.exam) else stringResource(R.string.semester),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.width(textModifierWidth)
                    )
                    HoursTrack(
                        totalHours = totalHours,
                        expectedHours = expectedHours,
                        hoursDone = hoursDone,
                        modifier = Modifier.weight(1f)
                    )
                    Row(
                        Modifier.width(hoursDiffWidth)
                    ) {
                        Text(
                            text = prefix,
                            style = numberStyle,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = value,
                            style = numberStyle

                        )
                    }
                }
                if (course.plannedHoursPerWeek > 0.0) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        val (planPrefix, planValue) = formatDiffText(hoursDone - planExpectedHours)
                        Text(
                            text = stringResource(R.string.plan),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.width(textModifierWidth)
                        )
                        HoursTrack(
                            totalHours = planTotalHours,
                            hoursDone = hoursDone,
                            expectedHours = planExpectedHours,
                            modifier = Modifier.weight(1f)
                        )
                        Row(
                            Modifier.width(hoursDiffWidth)
                        ) {
                            Text(
                                text = planPrefix,
                                style = numberStyle,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = planValue,
                                style = numberStyle
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun formatDiffText(diff: Double): Pair<String, String> {
    return if (diff >= 0) "" to stringResource(R.string.hours_diff, formatHoursDiff(diff))
    else stringResource(R.string.to_go) to stringResource(R.string.hours_diff, formatHours(abs(diff)))
}
