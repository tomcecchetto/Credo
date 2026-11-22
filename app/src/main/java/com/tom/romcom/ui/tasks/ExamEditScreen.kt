package com.tom.romcom.ui.tasks

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tom.romcom.R
import com.tom.romcom.ui.CredoTopBar
import com.tom.romcom.ui.DateField
import com.tom.romcom.ui.TimeField

@Composable
fun ExamEditScreen(
    examId: Int,
    onBack: () -> Unit,
    onSaved: () -> Unit,
    onDeleted: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ExamEditViewModel = viewModel()
) {
    LaunchedEffect(examId) {
        viewModel.load(examId)
    }

    var showDeleteDialog by rememberSaveable { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(stringResource(R.string.delete_exam)) },
            text = { Text(stringResource(R.string.cannot_be_undone)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.delete(onDone = onDeleted); showDeleteDialog = false
                    }) {
                    Text(stringResource(R.string.delete))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false }
                ) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            CredoTopBar(
                title = viewModel.courseName,
                windowInsets = TopAppBarDefaults.windowInsets,
                onBack = onBack,
                onSave = { viewModel.save(onDone = onSaved)},
                onDelete = { showDeleteDialog = true },
                saveEnabled = viewModel.canSave
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
                    detectTapGestures(onTap = { focusManager.clearFocus() })
                }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp)
                    .padding(bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DateField(
                    label = stringResource(R.string.exam_date),
                    epochDay = viewModel.epochDay,
                    onDateSelected = { viewModel.onEpochDayChange(it) }
                )
                TimeField(
                    label = stringResource(R.string.start_time_optional),
                    minuteOfDay = viewModel.startMinuteOfDay,
                    onTimeSelected = { viewModel.onStartMinuteOfDayChange(it) },
                    onClear = { viewModel.onStartMinuteOfDayClear() }
                )
                TimeField(
                    label = stringResource(R.string.end_time_optional),
                    minuteOfDay = viewModel.endMinuteOfDay,
                    onTimeSelected = { viewModel.onEndMinuteOfDayChange(it) },
                    onClear = { viewModel.onEndMinuteOfDayClear() }
                )
                OutlinedTextField(
                    value = viewModel.room,
                    onValueChange = { viewModel.onRoomChange(it) },
                    label = { Text(stringResource(R.string.room)) },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }
}
