package com.tom.romcom.ui.semester

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tom.romcom.R
import com.tom.romcom.ui.CredoTopBar
import com.tom.romcom.ui.DateField

@Composable
fun SemesterSetupScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SemesterSetupViewModel = viewModel()

) {
    var showStartPicker by rememberSaveable { mutableStateOf(false) }
    val startPickerState = rememberDatePickerState()
    var showEndPicker by rememberSaveable { mutableStateOf(false) }
    val endPickerState = rememberDatePickerState()

    if (showEndPicker) {
        DatePickerDialog(
            onDismissRequest = { showEndPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val millis = endPickerState.selectedDateMillis
                    if (millis != null) {
                        viewModel.onEndDateSelected(millis)
                    }
                    showEndPicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = {
                    showEndPicker = false
                }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        ) {
            DatePicker(state = endPickerState)
        }
    }

    if (showStartPicker) {
        DatePickerDialog(
            onDismissRequest = {showStartPicker = false},
            confirmButton = {
                TextButton(onClick = {
                    val millis = startPickerState.selectedDateMillis
                    if (millis != null) {
                        viewModel.onStartDateSelected(millis)
                    }
                    showStartPicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                    showStartPicker = false})
                {
                    Text(stringResource(R.string.cancel))
                }
            }
        ) {
            DatePicker(state = startPickerState)
        }
    }

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0,0,0,0),
        topBar = { CredoTopBar(
            stringResource(R.string.details),
            windowInsets = TopAppBarDefaults.windowInsets,
            onBack = onBack)
        }
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            DateField(
                label = stringResource(R.string.start_of_semester),
                epochDay = viewModel.startEpochDay,
                onClick = { showStartPicker = true },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            DateField(
                label = stringResource(R.string.end_of_semester),
                epochDay = viewModel.endEpochDay,
                onClick = { showEndPicker = true },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            OutlinedTextField(
                value = viewModel.hoursPerCredit,
                onValueChange = { viewModel.onHoursChange(it) },
                label = { Text(stringResource(R.string.hours_per_credit)) },
                placeholder = { Text("30") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Text(
                    text = stringResource(R.string.count_lecture_hours),
                    modifier = Modifier.weight(1f))
                Switch(
                    checked = viewModel.countLectureHours,
                    onCheckedChange = { viewModel.onCountLectureHoursChange(it) },
                )
            }
        }
    }
}