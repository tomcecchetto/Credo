package com.tom.romcom.ui


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.tom.romcom.R
import com.tom.romcom.ui.calendar.CalendarScreen
import com.tom.romcom.ui.progress.ProgressScreen
import com.tom.romcom.ui.tasks.TaskScreen

enum class Tab {
    PROGRESS, CALENDAR, TASK
}


@Composable
fun MainArea(
    onAddCourse: () -> Unit,
    onAddSemester: () -> Unit,
    onCourseClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by rememberSaveable { mutableStateOf(Tab.PROGRESS) }


    Scaffold(
        modifier = modifier,
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedTab == Tab.PROGRESS,
                    onClick = { selectedTab = Tab.PROGRESS },
                    label = {Text(stringResource(R.string.progress))},
                    icon = { Icon(painterResource(
                        R.drawable.ic_progress),
                        contentDescription = null) }
                )
                NavigationBarItem(
                    selected = selectedTab == Tab.CALENDAR,
                    onClick = { selectedTab = Tab.CALENDAR },
                    label = {Text(stringResource(R.string.calendar))},
                    icon = { Icon(painterResource(
                        R.drawable.ic_calenar),
                        contentDescription = null) }
                )
                NavigationBarItem(
                    selected = selectedTab == Tab.TASK,
                    onClick = { selectedTab = Tab.TASK },
                    label = { Text(stringResource(R.string.tasks))},
                    icon = {
                        Icon(
                            painterResource(R.drawable.ic_tasks),
                            contentDescription = null
                        )
                    }

                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier
            .padding(innerPadding)
        ) {
            when (selectedTab) {
                Tab.PROGRESS -> ProgressScreen(
                    onAddSemester = onAddSemester,
                    onAddCourse = onAddCourse,
                    onCourseClick = onCourseClick)
                Tab.CALENDAR -> CalendarScreen()
                Tab.TASK -> TaskScreen()
            }
        }
    }
}