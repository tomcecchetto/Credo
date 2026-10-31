package com.tom.romcom.ui.calendar

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tom.romcom.R
import com.tom.romcom.ui.CredoTopBar
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale


@Composable
fun TimetableSetupScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    viewModel: CalendarViewModel = viewModel()
) {
    val slotsWithInfo by viewModel.slotsWithInfo.collectAsState(initial = emptyList())
    val courses by viewModel.courses.collectAsState(initial = emptyList())
    val byDay = remember(slotsWithInfo) { slotsWithInfo.groupBy { it.slot.dayOfWeek } }
    val message = viewModel.importMessage


    val picker = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult
        else viewModel.importCalendar(uri)
    }
    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets.navigationBars,
        topBar = {
            CredoTopBar(
                title = stringResource(R.string.timetable),
                onImport = { picker.launch(arrayOf("text/calendar")) },
                onBack = onBack,
                windowInsets = TopAppBarDefaults.windowInsets
            )
        }, floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            if (slotsWithInfo.isNotEmpty()) {
                ExtendedFloatingActionButton(onClick = onBack) {
                    Text(stringResource(R.string.done))
                }
            }
        }
    ) { innerPadding ->
        when {
            slotsWithInfo.isEmpty() ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(
                        16.dp,
                        Alignment.CenterVertically
                    )
                ) {
                    Text(
                        text = stringResource(R.string.calendar_empty),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 32.dp)
                    )
                    Button(onClick = { picker.launch(arrayOf("text/calendar")) }) {
                        Text(stringResource(R.string.import_calendar))
                    }
                    if (message != null) {
                        Text(
                            text = message,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 32.dp)
                        )
                    }
                }

            else -> LazyColumn(
                modifier = Modifier
                    .padding(innerPadding),
                contentPadding = PaddingValues(bottom = 88.dp)
            ) {
                item {
                    if (message != null) {
                        Text(
                            text = message,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
                for ((day, slots) in byDay) {
                    item(key = "day-$day") {
                        Text(
                            text = DayOfWeek.of(day)
                                .getDisplayName(TextStyle.FULL, Locale.getDefault()),
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                    items(items = slots, key = { it.slot.id }) { info ->
                        SlotRow(
                            info = info,
                            onAttendingChange = { checked ->
                                viewModel.setAttending(
                                    info.slot,
                                    checked
                                )
                            },
                            courses = courses,
                            onCourseSelected = { courseId ->
                                viewModel.setCourseId(
                                    info.slot,
                                    courseId = courseId
                                )
                            },
                            onCreateCourse = { name, credits ->
                                viewModel.createCourseForSlot(
                                    info.slot,
                                    name = name,
                                    credits = credits
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}
