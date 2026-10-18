package com.tom.romcom.ui.course

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tom.romcom.R
import com.tom.romcom.ui.CredoTopBar
import com.tom.romcom.ui.DateField
import com.tom.romcom.ui.courseColors

@Composable
fun CourseSetupScreen(
    courseId : Int?,
    onSaved: () -> Unit,
    onDeleted: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CourseSetupViewModel = viewModel()
) {
    LaunchedEffect(courseId) {
        viewModel.load(courseId)
    }
    val focusManager = LocalFocusManager.current
    var showDeleteDialog by rememberSaveable { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(stringResource(R.string.discard_course)) },
            text = { Text(stringResource(R.string.cannot_be_undone)) },
            confirmButton = {
                TextButton(onClick = { viewModel.delete(onDone = onDeleted) ; showDeleteDialog = false}) {
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
        contentWindowInsets = WindowInsets(0,0,0,0),
        topBar = {
            CredoTopBar(
                title = stringResource(R.string.course_details),
                windowInsets = TopAppBarDefaults.windowInsets,
                onBack = onBack,
                saveEnabled = viewModel.canSave,
                onSave = {viewModel.save(onDone = onSaved)},
                onDelete = if (courseId != null) { { showDeleteDialog = true } } else null
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
            OutlinedTextField(
                label = { Text(stringResource(R.string.course_name)) },
                value = viewModel.name,
                onValueChange = { viewModel.onNameChange(it) },
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences, imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = {focusManager.moveFocus(focusDirection = FocusDirection.Down)}),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            OutlinedTextField(
                label = { Text(stringResource(R.string.credits)) },
                value = viewModel.credits,
                onValueChange = { viewModel.onCreditsChange(it) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = {focusManager.moveFocus(focusDirection = FocusDirection.Down)}),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            OutlinedTextField(
                label = { Text(stringResource(R.string.lecture_hours_per_week)) },
                value = viewModel.lectureHours,
                onValueChange = { viewModel.onLectureHoursChange(it) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {focusManager.clearFocus()}),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            DateField(
                label = stringResource(R.string.exam_date),
                epochDay = viewModel.examEpochDay,
                onDateSelected = { viewModel.onExamDateSelected(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                courseColors.forEach { color ->
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(shape = CircleShape)
                            .background(Color(color))
                            .clickable { viewModel.onColorChange(color) }
                            .border(
                                width = if (color == viewModel.color) 3.dp else 0.dp,
                                color = MaterialTheme.colorScheme.onSurface,
                                shape = CircleShape
                            )
                    )
                }
            }
        }
    }
}
