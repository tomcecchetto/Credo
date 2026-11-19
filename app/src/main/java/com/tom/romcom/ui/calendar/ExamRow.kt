package com.tom.romcom.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tom.romcom.R
import com.tom.romcom.data.ExamWithCourse
import com.tom.romcom.ui.formatMinuteOfDay
import com.tom.romcom.ui.theme.LightPink
import com.tom.romcom.ui.theme.NearBlack
import com.tom.romcom.ui.theme.SoftRed

@Composable
fun ExamRow(
    exam: ExamWithCourse,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = SoftRed.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .background(color = SoftRed, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painterResource(R.drawable.ic_exam),
                contentDescription = null,
                tint = NearBlack,
                modifier = Modifier
                    .size(16.dp)
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp)
        ) {
            Text(
                text = stringResource(R.string.exam_row_title, exam.courseName),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
            val startTime = formatMinuteOfDay(exam.exam.startMinuteOfDay)
            val endTime = formatMinuteOfDay(exam.exam.endMinuteOfDay)
            val room = exam.exam.room
            val timePart = when {
                exam.exam.startMinuteOfDay == null -> null
                exam.exam.endMinuteOfDay == null -> startTime
                else -> "$startTime–$endTime"
            }
            val joined = listOfNotNull(timePart, room)
                .joinToString("  ·  ")

            if (joined.isNotEmpty()) {
                Text(
                    text = joined,
                    style = MaterialTheme.typography.bodySmall,
                    color = LightPink
                )
            }
        }
    }
}