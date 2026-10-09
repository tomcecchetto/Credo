package com.tom.romcom.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

enum class Tab {
    PROGRESS, CALENDAR, TASKS
}


@Composable
fun CredoApp(
    modifier: Modifier = Modifier
) {
    var isOn by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = true,
                    onClick = {isOn = true},
                    label = { Text("Progress") },
                    icon = { IconButtonDefaults.standardShape}
                )
            }
        }
    ) { innerPadding ->
        Box(Modifier
            .padding(innerPadding))
    }
}