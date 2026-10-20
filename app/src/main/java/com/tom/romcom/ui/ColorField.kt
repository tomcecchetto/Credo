package com.tom.romcom.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.toColorLong
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.tom.romcom.R

@Composable
fun ColorField(
    label: String, color: Long, onColorSelected: (Long) -> Unit, modifier: Modifier = Modifier
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }

    if (showDialog) {
        val controller = rememberColorPickerController()
        var picked by remember { mutableStateOf(Color(color)) }

        AlertDialog(
            onDismissRequest = { showDialog = false }, text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                HsvColorPicker(
                    controller = controller,
                    initialColor = Color(color),
                    onColorChanged = { envelope -> picked = envelope.color },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                )
                BrightnessSlider(
                    controller = controller,
                    modifier = Modifier
                        .height(35.dp)
                        .fillMaxWidth()
                )
            }
        }, confirmButton = {
            TextButton(
                onClick = {
                    onColorSelected(picked.toArgb().toLong() and 0xFFFFFFFFL)
                    showDialog = false
                }) {
                Text("OK")
            }
        },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            })
    }

    Box(modifier = modifier) {
        OutlinedTextField(
            value = "",
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            leadingIcon = {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(Color(color), shape = CircleShape)
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable { showDialog = true })
    }
}
