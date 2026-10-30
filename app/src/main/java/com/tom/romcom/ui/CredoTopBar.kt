package com.tom.romcom.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.tom.romcom.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun CredoTopBar(
    title: String,
    modifier: Modifier = Modifier,
    windowInsets: WindowInsets = WindowInsets(0, 0, 0, 0),
    onEdit: (() -> Unit)? = null,
    onBack: (() -> Unit)? = null,
    onSave: (() -> Unit)? = null,
    onDelete: (() -> Unit)? = null,
    onImport: (() -> Unit)? = null,
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
                    Icon(
                        painterResource(R.drawable.ic_back),
                        contentDescription = stringResource(R.string.back)
                    )
                }
            }
        },
        actions = {
            if (onDelete != null) {
                IconButton(onClick = onDelete) {
                    Icon(
                        painterResource(R.drawable.ic_delete),
                        contentDescription = stringResource(R.string.delete)
                    )
                }
            }
            if (onSave != null) {
                IconButton(
                    onClick = onSave,
                    enabled = saveEnabled,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                )
                {
                    Icon(
                        painterResource(R.drawable.ic_check),
                        contentDescription = stringResource(R.string.save)
                    )
                }
            }
            if (onImport != null) {
                IconButton(onClick = onImport) {
                    Icon(painterResource(R.drawable.ic_attach),
                        contentDescription = stringResource(R.string.import_calendar))
                }
            }
            if (onEdit != null) {
                IconButton(onClick = onEdit) {
                    Icon(painterResource(R.drawable.ic_edit),
                        contentDescription = stringResource(R.string.edit_semester))
                }
            }
        }
    )
}
