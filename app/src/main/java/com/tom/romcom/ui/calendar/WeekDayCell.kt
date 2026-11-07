package com.tom.romcom.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.Hyphens
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.tom.romcom.data.LectureWithCourse

@Composable
fun WeekDayCell(
    lecture: LectureWithCourse?,
    modifier: Modifier = Modifier
) {
    if (lecture == null) {
        Box(
            modifier = modifier
                .background(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.04f), shape = RoundedCornerShape(8.dp))
        )
    }
    else {
        Column(
            modifier = modifier
                .background(color = (Color(lecture.color).copy(alpha = 0.2f)), shape = RoundedCornerShape(8.dp))
                .border(1.dp, (Color(lecture.color).copy(alpha = 0.4f)), shape = RoundedCornerShape(8.dp))
                .padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = lecture.courseName,
                style = MaterialTheme.typography.labelSmall.copy(hyphens = Hyphens.Auto),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        }
    }
}