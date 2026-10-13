package com.tom.romcom.ui

import androidx.compose.material3.Icon
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tom.romcom.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun CredoTopBar(
    title: String,
    modifier: Modifier = Modifier,
    windowInsets: WindowInsets = WindowInsets(0,0,0,0),
    onBack: (() -> Unit)? = null,
    onSave: (() -> Unit)? = null,
    saveEnabled: Boolean = true

) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        windowInsets = windowInsets,
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            if (onBack != null) {
                IconButton(onClick = onBack) {
                    Icon(painterResource(R.drawable.ic_back),
                        contentDescription = stringResource(R.string.back))
                }
            }
        },
        actions = {
            if (onSave != null) {
            TextButton(
                onClick = onSave,
                enabled = saveEnabled,
                colors = ButtonDefaults.textButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary)
            ) { Text(stringResource(R.string.save)) } }
        }
    )
}
