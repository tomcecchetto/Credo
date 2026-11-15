package com.tom.romcom.ui.tasks

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tom.romcom.R
import com.tom.romcom.data.TaskType
import com.tom.romcom.ui.CredoTopBar


@Composable
fun TaskScreen(
    onAddTask: () -> Unit,
    onTaskClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TaskViewModel = viewModel()
) {
    val filteredOpenTasks by viewModel.openTasksByType.collectAsState(initial = emptyList())
    val filteredCompletedTasks by viewModel.completedTasksByType.collectAsState(initial = emptyList())
    val openTasks by viewModel.openTasks.collectAsState(initial = emptyList())
    val completedTasks by viewModel.completedTasks.collectAsState(initial = emptyList())
    val selectedTypes by viewModel.selectedTypes.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    var showDeleteDialog by rememberSaveable { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text(
                    pluralStringResource(
                        R.plurals.delete_tasks,
                        filteredCompletedTasks.size
                    )
                )
            },
            text = { Text(stringResource(R.string.cannot_be_undone)) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteTasks(filteredCompletedTasks); showDeleteDialog = false
                }) {
                    Text(stringResource(R.string.delete))
                }
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
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = { CredoTopBar(stringResource(R.string.tasks)) },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            ExtendedFloatingActionButton(onClick = onAddTask) {
                Icon(
                    painterResource(R.drawable.ic_add),
                    contentDescription = stringResource(R.string.add_task)
                )
                Text(stringResource(R.string.add_task))
            }
        }
    ) { innerPadding ->
        if (openTasks.isEmpty() && completedTasks.isEmpty()) {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.no_open_tasks),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding),
                contentPadding = PaddingValues(bottom = 88.dp)
            ) {
                item(key = "filter_chips") {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TaskType.entries.forEach { type ->
                            FilterChip(
                                selected = type in selectedTypes,
                                onClick = { viewModel.toggleType(type) },
                                label = { Text(taskLabel(type)) },
                                leadingIcon = if (type in selectedTypes) {
                                    {
                                        Icon(
                                            painterResource(R.drawable.ic_check),
                                            contentDescription = null
                                        )
                                    }
                                } else null
                            )
                        }
                    }
                }
                item(key = "open_header") {
                    Text(
                        text = if (filteredOpenTasks.isNotEmpty()) stringResource(
                            R.string.open_header,
                            filteredOpenTasks.size
                        ) else stringResource(R.string.open_header_done),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    )
                }
                if (filteredOpenTasks.isEmpty()) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(
                                6.dp,
                                alignment = Alignment.CenterHorizontally
                            )
                        ) {
                            Text(
                                text = if (openTasks.isEmpty()) stringResource(R.string.tasks_done)
                                else stringResource(R.string.no_open_tasks_filter),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                } else {

                    items(items = filteredOpenTasks, key = { it.task.id }) { taskWithCourse ->
                        TaskRow(
                            taskWithCourse = taskWithCourse,
                            onClick = { onTaskClick(taskWithCourse.task.id) },
                            onCheckedChange = { checked ->
                                viewModel.setCompleted(
                                    taskWithCourse.task,
                                    checked
                                )
                            }
                        )
                    }
                }
                item("done_header") {
                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .clickable(onClick = { expanded = !expanded })
                                .padding(start = 16.dp, end = 4.dp)
                                .padding(vertical = 0.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            painterResource(R.drawable.ic_chevron_right),
                            contentDescription = null,
                            modifier = if (expanded) Modifier.rotate(90f) else Modifier.rotate(
                                0f
                            )
                        )
                        Text(
                            text = if (filteredCompletedTasks.isEmpty()) stringResource(R.string.done_header_empty) else stringResource(
                                R.string.done_header,
                                filteredCompletedTasks.size
                            ),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = { showDeleteDialog = true },
                            enabled = filteredCompletedTasks.isNotEmpty()
                        ) {
                            Icon(
                                painterResource(R.drawable.ic_delete),
                                contentDescription = stringResource(R.string.delete)
                            )
                        }
                    }
                }

                if (expanded) {
                    items(items = filteredCompletedTasks, key = { it.task.id }) { taskWithCourse ->
                        TaskRow(
                            taskWithCourse = taskWithCourse,
                            onClick = { onTaskClick(taskWithCourse.task.id) },
                            onCheckedChange = { checked ->
                                viewModel.setCompleted(
                                    task = taskWithCourse.task,
                                    completed = checked
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}
