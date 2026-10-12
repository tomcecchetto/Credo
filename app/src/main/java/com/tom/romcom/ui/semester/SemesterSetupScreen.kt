package com.tom.romcom.ui.semester

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tom.romcom.R
import com.tom.romcom.ui.CredoTopBar

@Composable
fun SemesterSetupScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SemesterSetupViewModel = viewModel()

) {
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
            OutlinedTextField(
                value = viewModel.hoursPerCredit,
                onValueChange = { viewModel.onHoursChange(it) },
                label = { Text(stringResource(R.string.hours_per_credit)) },
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
                    text = stringResource(R.string.count_lecture_hourss),
                    modifier = Modifier.weight(1f))
                Switch(
                    checked = viewModel.countLectureHours,
                    onCheckedChange = { viewModel.onCountLectureHoursChange(it) },
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                )
            }
        }
    }
}