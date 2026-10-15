package com.tom.romcom.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.tom.romcom.ui.course.CourseSetupScreen
import com.tom.romcom.ui.semester.SemesterSetupScreen

@Composable
fun CredoApp() {
    val backStack = rememberNavBackStack(MainAreaRoute)

    NavDisplay(
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<MainAreaRoute> {
                MainArea(
                    onAddSemester = { backStack.add(SemesterSetupRoute) },
                    onAddCourse = { backStack.add(CourseSetupRoute()) },
                    onCourseClick = { id -> backStack.add(CourseSetupRoute(id)) }
                )
            }
            entry<SemesterSetupRoute> {
                SemesterSetupScreen(
                    onBack = { backStack.removeLastOrNull() },
                    onSaved = { backStack.removeLastOrNull() })
            }
            entry<CourseSetupRoute> { route ->
                CourseSetupScreen(
                    onBack = { backStack.removeLastOrNull() },
                    onSaved = { backStack.removeLastOrNull() },
                    courseId = route.courseId
                )
            }
        }
    )
}