package com.tom.romcom.ui.tasks

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.tom.romcom.R
import com.tom.romcom.data.TaskType

@Composable
fun taskLabel(task: TaskType): String = when (task) {
    TaskType.HOMEWORK -> stringResource(R.string.homework)
    TaskType.PROJECT -> stringResource(R.string.project)
    TaskType.LAB -> stringResource(R.string.lab)
}
