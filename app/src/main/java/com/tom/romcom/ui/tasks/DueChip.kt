package com.tom.romcom.ui.tasks

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tom.romcom.ui.formatMinuteOfDay
import com.tom.romcom.ui.theme.Amber
import com.tom.romcom.ui.theme.CredoTheme
import java.time.LocalDate

@Composable
fun DueChip(
    dueStatus: DueStatus, dueEpochDay: Long, dueMinuteOfDay: Int?, modifier: Modifier = Modifier
) {
    val day = dueLabelText(dueLabel = dueStatus.day, dueEpochDay = dueEpochDay)
    val label = if (dueMinuteOfDay != null && dueStatus.level == Urgency.SOON) "$day ${formatMinuteOfDay(dueMinuteOfDay)}"
    else day
    val backgroundColor = when (dueStatus.level) {
        Urgency.OVERDUE -> MaterialTheme.colorScheme.error.copy(alpha = 0.18f)
        Urgency.SOON -> Amber.copy(alpha = 0.16f)
        Urgency.LATER -> Color.Transparent
    }
    val textColor = when (dueStatus.level) {
        Urgency.OVERDUE -> MaterialTheme.colorScheme.error
        Urgency.SOON -> Amber
        Urgency.LATER -> MaterialTheme.colorScheme.onSurfaceVariant
    }
    Text(
        text = label,
        color = textColor,
        style = MaterialTheme.typography.labelMedium,
        modifier = modifier
            .background(color = backgroundColor, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DueChipPreview() {
    CredoTheme {
        DueChip(
            dueStatus = DueStatus(level = Urgency.OVERDUE, day = DueLabel.YESTERDAY),
            dueEpochDay = LocalDate.of(2026, 11, 10).toEpochDay(),
            dueMinuteOfDay = 600
        )
    }
}
