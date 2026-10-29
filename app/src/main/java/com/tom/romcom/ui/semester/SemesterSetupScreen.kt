package com.tom.romcom.ui.semester

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tom.romcom.R
import com.tom.romcom.ui.CredoTopBar
import com.tom.romcom.ui.DateField

@Composable
fun SemesterSetupScreen(
    onSaved: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SemesterSetupViewModel = viewModel()

) {
    LaunchedEffect(Unit) {
        viewModel.load()
    }
    val focusManager = LocalFocusManager.current
    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0,0,0,0),
        topBar = { CredoTopBar(
            stringResource(R.string.details),
            windowInsets = TopAppBarDefaults.windowInsets,
            onBack = onBack,
            saveEnabled = viewModel.canSave,
            onSave = { viewModel.save(onDone = onSaved) },
            )
        }
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .pointerInput(Unit) {
                    detectTapGestures(onTap = { focusManager.clearFocus() })
                }
        ) {
            DateField(
                label = stringResource(R.string.start_of_semester),
                epochDay = viewModel.startEpochDay,
                onDateSelected = { viewModel.onStartDateSelected(it) },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            DateField(
                label = stringResource(R.string.end_of_semester),
                epochDay = viewModel.endEpochDay,
                onDateSelected = { viewModel.onEndDateSelected(it) },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            OutlinedTextField(
                label = { Text(stringResource(R.string.lecture_weeks)) },
                value = viewModel.lectureWeeks,
                onValueChange = { viewModel.onLectureWeeksChange(it) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = {focusManager.moveFocus(focusDirection = FocusDirection.Down)}),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            OutlinedTextField(
                label = { Text(stringResource(R.string.hours_per_credit)) },
                value = viewModel.hoursPerCredit,
                onValueChange = { viewModel.onHoursChange(it) },
                placeholder = { Text("30") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus()}),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

        }
    }
}
