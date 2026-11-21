package com.tom.romcom.ui.tasks

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tom.romcom.R
import com.tom.romcom.data.ExamWithCourse
import com.tom.romcom.ui.formatDayHeader
import com.tom.romcom.ui.formatMinuteOfDay
import java.time.LocalDate

@Composable
fun ExamCard(
    exam: ExamWithCourse,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val today = LocalDate.now().toEpochDay()
    val daysUntilExam = (exam.exam.epochDay - today).toInt()
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .background(
                color = MaterialTheme.colorScheme.surfaceContainer
            )
            .padding(start = 12.dp, top = 10.dp, end = 14.dp, bottom = 10.dp)
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .fillMaxHeight()
                .background(
                    color = Color(exam.courseColor),
                    shape = RoundedCornerShape(2.dp)
                )
        )
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                text = exam.courseName,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            val date = formatDayHeader(exam.exam.epochDay)
            val startTime = formatMinuteOfDay(exam.exam.startMinuteOfDay)
            val room = exam.exam.room
            val timePart = if (exam.exam.startMinuteOfDay == null) null else startTime
            val joined = listOfNotNull(date, timePart, room)
                .joinToString("  ·  ")
            Text(
                text = joined,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (daysUntilExam != 0) "$daysUntilExam"
                else stringResource(R.string.today),
                style = if (daysUntilExam == 0) MaterialTheme.typography.bodySmall
                else MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
            if (daysUntilExam != 0) {
                Text(
                    text = pluralStringResource(R.plurals.days, daysUntilExam),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}