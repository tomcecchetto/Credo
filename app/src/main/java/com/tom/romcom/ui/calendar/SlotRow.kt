package com.tom.romcom.ui.calendar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.tom.romcom.data.Course
import com.tom.romcom.data.SlotWithInfo
import com.tom.romcom.ui.formatMinuteOfDay
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun SlotRow(
    info: SlotWithInfo,
    courses: List<Course>,
    onAttendingChange: (Boolean) -> Unit,
    onCourseSelected: (Int?) -> Unit
) {
    val day = DayOfWeek.of(info.slot.dayOfWeek).getDisplayName(TextStyle.SHORT, Locale.getDefault())
    val start = formatMinuteOfDay(info.slot.startMinuteOfDay)
    val end = formatMinuteOfDay(info.endMinuteOfDay)
    val faded = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
    val ticked = MaterialTheme.colorScheme.onSurface
    val textColor = if (info.slot.attending) ticked else faded


    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = info.slot.attending,
            onCheckedChange = { checked -> onAttendingChange(checked) }
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = info.slot.title,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleMedium,
                    color = textColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "$start - $end",
                    style = MaterialTheme.typography.bodyMedium,
                    color = textColor
                )
            }
                Text(
                    text = "${info.room} · ${info.lectureCount}×",
                    style = MaterialTheme.typography.bodySmall,
                    color = textColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            if (info.slot.attending) {
                CourseDropdown(
                    courses = courses,
                    selectedCourseId = info.slot.courseId,
                    onCourseSelected = onCourseSelected
                )
            }
        }
    }
}
