package com.tom.romcom.ui.tasks

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.tom.romcom.ui.CredoApp
import com.tom.romcom.ui.formatMinuteOfDay
import com.tom.romcom.ui.theme.CredoTheme
import java.time.LocalDate

@Composable
fun DueChip(
    dueStatus: DueStatus,
    dueEpochDay: Long,
    dueMinuteOfDay: Int?,
    modifier: Modifier = Modifier
) {
    val day = dueLabelText(dueLabel = dueStatus.day, dueEpochDay = dueEpochDay)
    val label = if (dueMinuteOfDay != null) "$day ${formatMinuteOfDay(dueMinuteOfDay)}"
        else day
    Text(
        text = label,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun DueChipPreview() {
    CredoTheme {
        DueChip(
            dueStatus = DueStatus(level = Urgency.SOON, day = DueLabel.TODAY),
            dueEpochDay = LocalDate.of(2026, 11, 11).toEpochDay(),
            dueMinuteOfDay = 600
        )
    }
}
