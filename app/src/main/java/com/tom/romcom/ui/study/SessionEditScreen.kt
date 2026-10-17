package com.tom.romcom.ui.study

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tom.romcom.R
import com.tom.romcom.ui.CredoTopBar
import com.tom.romcom.ui.DateField
import com.tom.romcom.ui.TimeField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionEditScreen(
    onDeleted: () -> Unit,
    sessionId: Int? = null,
    onSaved: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SessionEditViewModel = viewModel()
) {
    LaunchedEffect(sessionId) {
        viewModel.load(sessionId)
    }
    val courses by viewModel.courses.collectAsState(initial = emptyList())
    var showDeleteDialog by rememberSaveable { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(stringResource(R.string.delete_session)) },
            text = { Text(stringResource(R.string.cannot_be_undone)) },
            confirmButton = {
                TextButton(onClick = { viewModel.delete(onDone = onDeleted) ; showDeleteDialog = false })
                { Text(stringResource(R.string.delete)) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0,0,0,0),
        topBar = {
            CredoTopBar(
                title = stringResource(R.string.study_session),
                windowInsets = TopAppBarDefaults.windowInsets,
                onBack = onBack,
                onSave = { viewModel.save(onDone = onSaved) },
                saveEnabled = viewModel.canSave,
                onDelete = if (sessionId != null) { { showDeleteDialog = true } } else null
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (courses.isEmpty()) { Text(stringResource(R.string.add_course_first)) }
                else {
                    var open by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = open,
                        onExpandedChange = { open = it }
                    ) {
                        val courseName = courses.firstOrNull{ it.id == viewModel.courseId }?.name

                        OutlinedTextField(
                            value = courseName ?: "",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text(stringResource(R.string.course_name)) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(open) },
                            modifier = Modifier
                                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = open,
                            onDismissRequest = { open = false }
                        ) {
                            courses.forEach { course ->
                                DropdownMenuItem(
                                    text = { Text(course.name) },
                                    onClick = {
                                        viewModel.onCourseSelected(course.id)
                                        open = false
                                    }
                                )
                            }
                        }
                    }
                    DateField(
                        label = stringResource(R.string.date),
                        epochDay = viewModel.epochDay,
                        onDateSelected = { viewModel.onDateSelected(it) }
                    )
                    TimeField(
                        label = stringResource(R.string.start_time),
                        minuteOfDay = viewModel.startMinute,
                        onTimeSelected = { viewModel.onStartTimeSelected(it) }
                    )
                    TimeField(
                        label = stringResource(R.string.end_time),
                        minuteOfDay = viewModel.endMinute,
                        onTimeSelected = { viewModel.onEndTimeSelected(it) }
                    )
                }
            }
        }
    }
}
