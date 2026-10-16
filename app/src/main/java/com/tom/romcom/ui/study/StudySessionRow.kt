package com.tom.romcom.ui.study

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tom.romcom.data.Course
import com.tom.romcom.data.StudySession
import com.tom.romcom.ui.formatDateTime
import com.tom.romcom.ui.formatDuration

@Composable
fun StudySessionRow(
    session: StudySession,
    course: Course,
    modifier: Modifier = Modifier,
) {
    val endTime = session.endTime ?: return
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
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
                    text = formatDateTime(session.startTime),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Text(
                text = formatDuration(endTime - session.startTime),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}