package com.tom.romcom.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.tom.romcom.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun CredoTopBar(
    title: String,
    modifier: Modifier = Modifier,
    windowInsets: WindowInsets = WindowInsets(0, 0, 0, 0),
    onEdit: (() -> Unit)? = null,
    editDescription: String = stringResource(R.string.edit),
    onBack: (() -> Unit)? = null,
    onExportBackup: (() -> Unit)? = null,
    onImportBackup: (() -> Unit)? = null,
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
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
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
                    Icon(
                        painterResource(R.drawable.ic_attach),
                        contentDescription = stringResource(R.string.import_calendar)
                    )
                }
            }
            if (onEdit != null) {
                IconButton(onClick = onEdit) {
                    Icon(
                        painterResource(R.drawable.ic_edit),
                        contentDescription = editDescription
                    )
                }
            }
            if (onExportBackup != null || onImportBackup != null) {
                var open by remember { mutableStateOf(false) }
                Box {
                    IconButton(onClick = { open = true }) {
                        Icon(
                            painterResource(R.drawable.ic_more_vert),
                            contentDescription = stringResource(R.string.more_options)
                        )
                    }
                    DropdownMenu(
                        expanded = open,
                        onDismissRequest = { open = false }
                    ) {
                        if (onExportBackup != null) {
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.export_backup)) },
                                onClick = {
                                    onExportBackup()
                                    open = false
                                }
                            )
                        }
                        if (onImportBackup != null) {
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.import_backup)) },
                                onClick = {
                                    onImportBackup()
                                    open = false
                                }
                            )
                        }
                    }
                }
            }
        }
    )
}
