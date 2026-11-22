package com.tom.romcom.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tom.romcom.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun TimeField(
    label: String,
    minuteOfDay: Int?,
    onTimeSelected: (Int) -> Unit,
    onClear: (() -> Unit)? = null,
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
        val showClear = onClear != null && minuteOfDay != null
        OutlinedTextField(
            value = formatMinuteOfDay(minuteOfDay),
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = if (onClear != null && minuteOfDay != null) {{
                IconButton(onClick = onClear) {
                    Icon(painterResource(R.drawable.ic_close), contentDescription = stringResource(R.string.clear_time))
                }
            }} else null,
            modifier = Modifier.fillMaxWidth()
        )
        Box(
            Modifier
                .matchParentSize()
                .padding(end = if (showClear) 48.dp else 0.dp)
                .clickable(onClick = { showDialog = true })

        )
    }

}
