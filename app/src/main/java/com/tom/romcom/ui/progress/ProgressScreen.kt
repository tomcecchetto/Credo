package com.tom.romcom.ui.progress

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tom.romcom.R

@Composable
fun ProgressScreen(

    modifier: Modifier = Modifier,
    viewModel: ProgressViewModel = viewModel()
) {
    val courses by viewModel.courses.collectAsState(initial = emptyList())

    when {
        courses.isEmpty() -> Text(stringResource(R.string.No_courses_yet))
        else -> {}
    }
}