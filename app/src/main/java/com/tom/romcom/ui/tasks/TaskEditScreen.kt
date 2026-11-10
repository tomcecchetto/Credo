package com.tom.romcom.ui.tasks

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults.itemShape
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tom.romcom.R
import com.tom.romcom.data.TaskType
import com.tom.romcom.ui.CredoTopBar
import com.tom.romcom.ui.DateField
import com.tom.romcom.ui.TimeField
import com.tom.romcom.ui.calendar.CourseDropdown

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskEditScreen(
    taskId: Int? = null,
    onBack: () -> Unit,
    onSaved: () -> Unit,
    onDeleted: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TaskEditViewModel = viewModel()
) {
    LaunchedEffect(taskId) {
        viewModel.load(taskId)
    }

    val courses by viewModel.courses.collectAsState(initial = emptyList())
    var showDeleteDialog by rememberSaveable { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(stringResource(R.string.delete_task)) },
            text = { Text(stringResource(R.string.cannot_be_undone)) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.delete(onDone = onDeleted); showDeleteDialog = false
                })
                { Text(stringResource(R.string.delete)) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false })
                { Text(stringResource(R.string.cancel)) }
            }
        )
    }
    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            CredoTopBar(
                title = if (taskId == null) stringResource(R.string.new_task) else stringResource(R.string.new_task),
                windowInsets = TopAppBarDefaults.windowInsets,
                onBack = onBack,
                onSave = { viewModel.save(onDone = onSaved) },
                onDelete = if (taskId != null) {
                    { showDeleteDialog = true }
                } else null
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .pointerInput(Unit) {
                    detectTapGestures(onTap =  { focusManager.clearFocus() })
                }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp)
                    .padding(bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SingleChoiceSegmentedButtonRow(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    TaskType.entries.forEachIndexed { index, task ->
                        SegmentedButton(
                            selected = task == viewModel.taskType,
                            onClick = { viewModel.onTaskTypeChange(task) },
                            shape = itemShape(index = index, count = TaskType.entries.size),
                            icon = {}
                        ) {
                            Text(taskLabel(task))
                        }
                    }
                }
                OutlinedTextField(
                    value = viewModel.taskTitle,
                    onValueChange = { viewModel.onTaskTitleChange(it) },
                    label = { Text(stringResource(R.string.task_title)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    modifier = Modifier
                        .fillMaxWidth()
                )
                CourseDropdown(
                    courses = courses,
                    selectedCourseId = viewModel.courseId,
                    onCourseSelected = { viewModel.onCourseIdChange(it) },
                    suggestedName = "",
                    onCreateCourse = { name, credits -> viewModel.createCourse(name, credits) },
                    modifier = Modifier.fillMaxWidth()
                )
                DateField(
                    label = stringResource(R.string.due_date),
                    epochDay = viewModel.dueDate,
                    onDateSelected = { viewModel.onDueDateChange(it) }
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Switch(
                        checked = viewModel.timeSet,
                        onCheckedChange = { viewModel.onTimeSetChange(it) }
                    )
                    Text(
                        text = stringResource(R.string.due_time_state),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (viewModel.timeSet) {
                    TimeField(
                        label = stringResource(R.string.due_time),
                        minuteOfDay = viewModel.dueTime,
                        onTimeSelected = { viewModel.onDueTimeChange(it) }
                    )
                }
                OutlinedTextField(
                    value = viewModel.notes ?: "",
                    onValueChange = { viewModel.onNotesChange(it) },
                    label = { Text(stringResource(R.string.notes)) },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences),
                    minLines = 3,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }
}
