package com.tom.romcom.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.tom.romcom.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun TimeField(
    label: String,
    minuteOfDay: Int?,
    onTimeSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }

    if (showDialog) {
        val pickerState = rememberTimePickerState(
            initialHour = (minuteOfDay ?: 0) / 60,
            initialMinute = (minuteOfDay ?: 0) % 60
        )
        AlertDialog(
            onDismissRequest = { showDialog = false },
            text = { TimePicker(state = pickerState) },
            confirmButton = {
                TextButton(onClick = {
                    onTimeSelected(pickerState.hour * 60 + pickerState.minute)
                    showDialog = false
                }) { Text("OK") }
            },
            dismissButton = { TextButton(onClick = { showDialog = false })
            {
                Text(stringResource(R.string.cancel))
            } }
        )
    }
    Box(modifier = modifier) {
        OutlinedTextField(
            value = formatMinuteOfDay(minuteOfDay),
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth()
        )
        Box(
            Modifier.matchParentSize().clickable { showDialog = true }
        )
    }

}
