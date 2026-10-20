package com.tom.romcom.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
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

@Composable
fun DateField(
    label: String,
    epochDay: Long?,
    onDateSelected: (Long) -> Unit,
    onClear: (() -> Unit)? = null,
    modifier: Modifier = Modifier,

    ) {
    var showDialog by rememberSaveable { mutableStateOf(false) }
    val pickerState = rememberDatePickerState()

    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    val millis = pickerState.selectedDateMillis
                    if (millis != null) {
                        onDateSelected(millis)
                    }
                    showDialog = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDialog = false
                }
                ) {
                    Text(stringResource(R.string.cancel))
                }
            }
        ) {
            DatePicker(state = pickerState)
        }
    }



    Box(modifier = modifier) {
        val showClear = epochDay != null && onClear != null
        OutlinedTextField(
            value = formatEpochDay(epochDay),
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = if (onClear != null && epochDay != null) {{
                IconButton(onClick = onClear) {
                    Icon(painterResource(R.drawable.ic_close), contentDescription = stringResource(R.string.clear))
                }
            }} else null,
            modifier = Modifier.fillMaxWidth()
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .padding(end = if (showClear) 48.dp else 0.dp)
                .clickable(onClick = { showDialog = true })
        )
    }
}
