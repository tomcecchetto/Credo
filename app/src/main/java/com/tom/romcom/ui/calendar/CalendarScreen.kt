package com.tom.romcom.ui.calendar

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tom.romcom.R
import com.tom.romcom.ui.CredoTopBar
import com.tom.romcom.ui.formatMinuteOfDay
import java.time.DayOfWeek
import java.util.Locale


@Composable
fun CalendarScreen(
    modifier: Modifier = Modifier,
    viewModel: CalendarViewModel = viewModel()
) {
    val slots by viewModel.slots.collectAsState(initial = emptyList())
    val lectures by viewModel.lectures.collectAsState(initial = emptyList())
    val slotsWithInfo by viewModel.slotsWithInfo.collectAsState(initial = emptyList())

    val context = LocalContext.current
    val picker = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) {
        uri ->
        if (uri == null) return@rememberLauncherForActivityResult
        val text = context.contentResolver.openInputStream(uri)?.bufferedReader()?.use { it.readText() }
        if (text == null) return@rememberLauncherForActivityResult
        viewModel.importCalendar(text)
    }
    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0,0,0,0),
        topBar = { CredoTopBar(stringResource(R.string.calendar)) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            item {
                Button(
                    onClick = { picker.launch(arrayOf("text/calendar")) }
                ) {
                    Text(stringResource(R.string.import_calendar))
                }
            }
            items(slotsWithInfo) { info -> SlotRow(info) }
        }
    }
}
