package com.tom.romcom.ui.semester

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.tom.romcom.R
import com.tom.romcom.ui.CredoTopBar

@Composable
fun SemesterSetupScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,

) {
    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0,0,0,0),
        topBar = { CredoTopBar(stringResource(R.string.add_semester)) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
        ) {}
    }
}