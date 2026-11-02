package com.tom.romcom.ui.calendar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tom.romcom.R
import com.tom.romcom.ui.CredoTopBar
import com.tom.romcom.ui.formatDayHeader
import com.tom.romcom.ui.formatEpochDay
import java.time.LocalDate
import java.time.LocalTime
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

@Composable
fun CalendarScreen(
    onEditTimetable: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CalendarViewModel = viewModel()
) {
    val lectures by viewModel.lectures.collectAsState(initial = emptyList())
    val selectedDay by viewModel.selectedDay.collectAsState()
    val today = LocalDate.now().toEpochDay()
    val currentMinuteOfDay = LocalTime.now().hour * 60 + LocalTime.now().minute

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            CredoTopBar(
                title = stringResource(R.string.calendar),
                onEdit = onEditTimetable,
                editDescription = stringResource(R.string.edit_timetable)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { viewModel.previousDay() }) {
                    Icon(
                        painterResource(R.drawable.ic_chevron_left),
                        contentDescription = stringResource(R.string.previous_day)
                    )
                }
                Text(
                    text = when {
                        (selectedDay == today) -> stringResource(R.string.today)
                        (selectedDay == today + 1) -> stringResource(R.string.tomorrow)
                        (selectedDay == today - 1) -> stringResource(R.string.yesterday)
                        else -> formatDayHeader(selectedDay)
                    },
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .clickable(onClick = { viewModel.goToToday() })
                        .weight(1f)
                )
                IconButton(onClick = { viewModel.nextDay() }) {
                    Icon(
                        painterResource(R.drawable.ic_chevron_right),
                        contentDescription = stringResource(R.string.next_day)
                    )
                }
            }
            if (lectures.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(R.string.no_lectures),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(lectures) { lecture ->
                        LectureRow(lecture = lecture, isPast = (selectedDay == today && currentMinuteOfDay > lecture.endMinute) || selectedDay < today)
                    }
                }
            }
        }
    }
}
