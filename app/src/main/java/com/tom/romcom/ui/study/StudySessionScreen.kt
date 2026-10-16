package com.tom.romcom.ui.study

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tom.romcom.R
import com.tom.romcom.ui.CredoTopBar
import com.tom.romcom.ui.formatDuration
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudySessionScreen(
    modifier: Modifier = Modifier,
    viewModel: StudyViewModel = viewModel()
) {
    val runningSession by viewModel.runningSession.collectAsState(initial = null)
    val courses by viewModel.courses.collectAsState(initial = emptyList())

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            CredoTopBar(
                title = stringResource(R.string.study_session),
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
        ) {
            // local copy so Kotlin can smart-cast the delegated value
            val running = runningSession
            if (running == null) {
                if (courses.isEmpty()) {
                    Text(stringResource(R.string.add_course_first))
                } else {
                    var expanded by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = it }
                    ) {
                        val courseName =
                            courses.firstOrNull { it.id == viewModel.selectedCourseId }?.name
                        OutlinedTextField(
                            value = courseName ?: "",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text(stringResource(R.string.course_name)) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable)
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            courses.forEach { course ->
                                DropdownMenuItem(
                                    text = { Text(course.name) },
                                    onClick = {
                                        viewModel.onCourseSelected(course.id)
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                    Button(
                        onClick = {
                            viewModel.start()
                        },
                        enabled = courses.any { it.id == viewModel.selectedCourseId }
                    ) {
                        Text(stringResource(R.string.start))
                    }
                }
            } else {
                var now by remember { mutableLongStateOf(System.currentTimeMillis()) }
                LaunchedEffect(running.id) {
                    while (true) {
                        now = System.currentTimeMillis()
                        delay(duration = 1000.milliseconds)
                    }
                }
                val courseName = courses.firstOrNull { it.id == running.courseId }?.name
                Text(
                    text = courseName ?: "",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = formatDuration(now - running.startTime),
                    style = MaterialTheme.typography.displayMedium
                )
                Button(
                    onClick = { viewModel.stop() },
                ) {
                    Text(stringResource(R.string.stop))
                }
            }
        }

    }
}