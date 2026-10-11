package com.tom.romcom.ui

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.tom.romcom.ui.semester.SemesterSetupScreen

@Composable
fun CredoApp() {
    val backStack = rememberNavBackStack(MainAreaRoute)

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<MainAreaRoute> {
                MainArea(onAddSemester = { backStack.add(SemesterSetupRoute)})
            }
            entry<SemesterSetupRoute> {
                SemesterSetupScreen(onBack = {backStack.removeLastOrNull()})
            }
        }
    )
}