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
import androidx.compose.ui.unit.dp
import com.tom.romcom.data.SlotWithInfo
import com.tom.romcom.ui.formatMinuteOfDay
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun SlotRow(
    info: SlotWithInfo,
    onAttendingChange: (Boolean) -> Unit,
) {
    val day = DayOfWeek.of(info.slot.dayOfWeek).getDisplayName(TextStyle.SHORT, Locale.getDefault())
    val start = formatMinuteOfDay(info.slot.startMinuteOfDay)
    val end = formatMinuteOfDay(info.endMinuteOfDay)

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
            Text(
                text = info.slot.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "$day $start–$end · ${info.room} · ${info.lectureCount}×",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
