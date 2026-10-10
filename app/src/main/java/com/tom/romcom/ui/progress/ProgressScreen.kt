package com.tom.romcom.ui.progress

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.tom.romcom.R

@Composable
fun ProgressScreen(
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(R.string.Progress),
        modifier = modifier)
}