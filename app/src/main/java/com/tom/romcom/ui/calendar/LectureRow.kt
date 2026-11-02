package com.tom.romcom.ui.calendar

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tom.romcom.data.LectureWithCourse
import com.tom.romcom.ui.formatMinuteOfDay

@Composable
fun LectureRow(
    lecture: LectureWithCourse,
    isPast: Boolean
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .alpha(if (isPast) 0.5f else 1f),
        verticalAlignment = Alignment.Top
    ) {
        Column(
            modifier = Modifier
                .width(56.dp)
                .padding(top = 12.dp)
        ) {
            Text(
                text = formatMinuteOfDay(lecture.startMinute),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = formatMinuteOfDay(lecture.endMinute),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Card(
            modifier = Modifier.weight(1f),
            colors = CardDefaults.cardColors(containerColor = Color(lecture.color).copy(alpha = 0.2f)),
            border = BorderStroke(color = Color(lecture.color).copy(alpha = 0.4f), width = 1.dp)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = lecture.courseName,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = lecture.room,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
