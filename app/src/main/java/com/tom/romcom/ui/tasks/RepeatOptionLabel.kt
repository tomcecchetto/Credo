package com.tom.romcom.ui.tasks

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.tom.romcom.R

@Composable
fun repeatOptionLabel(repeatOption: RepeatOption): String = when (repeatOption) {
    RepeatOption.NONE -> stringResource(R.string.repeat_once)
    RepeatOption.WEEKLY -> stringResource(R.string.repeat_weekly)
    RepeatOption.BIWEEKLY -> stringResource(R.string.repeat_biweekly)
}