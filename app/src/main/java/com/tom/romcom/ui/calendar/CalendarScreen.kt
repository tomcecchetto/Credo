package com.tom.romcom.ui.calendar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.tom.romcom.R
import com.tom.romcom.ui.CredoTopBar

@Composable
fun CalendarScreen(
    onEditTimetable: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            CredoTopBar(
                title = stringResource(R.string.calendar),
                onEdit = onEditTimetable,
                editDescription = stringResource(R.string.edit_timetable)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) { }
    }
}