package com.tom.romcom.ui.progress

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tom.romcom.R
import com.tom.romcom.ui.CredoTopBar
import com.tom.romcom.ui.course.CourseCard
import com.tom.romcom.ui.formatHours
import com.tom.romcom.ui.theme.PurpleHeart
import java.time.LocalDate

@Composable
fun ProgressScreen(
    onAddCourse: () -> Unit,
    onAddSemester: () -> Unit,
    onCourseClick: (Int) -> Unit,
    onImportCalendar: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProgressViewModel = viewModel()

) {
    val courses by viewModel.courses.collectAsState(initial = emptyList())
    val semester by viewModel.semester.collectAsState(initial = null)
    val exams by viewModel.exams.collectAsState(initial = emptyList())
    val classHoursPerCourse by viewModel.classHoursPerCourse.collectAsState(initial = emptyList())
    val finishedSessions by viewModel.finishedSessions.collectAsState(initial = emptyList())
    val hoursThisWeek = hoursThisWeek(finishedSessions)
    val reachedCourseGoals = courses.count { isWeeklyGoalReached(it, finishedSessions) }

    val today = LocalDate.now().toEpochDay()

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            CredoTopBar(
                title = stringResource(R.string.progress),
                onEdit = if (semester != null) onAddSemester else null
            )
        }, floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            if (courses.isNotEmpty()) {
                ExtendedFloatingActionButton(onClick = onAddCourse) {
                    Icon(
                        painterResource(R.drawable.ic_add),
                        contentDescription = stringResource(R.string.add_course)
                    )
                    Text(stringResource(R.string.add_course))
                }
            }
        }) { innerPadding ->
        val currentSemester = semester
        when {
            currentSemester == null -> Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.welcome_title),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.welcome_intro),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(24.dp))
                Column(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    val steps = listOf(
                        stringResource(R.string.welcome_step_1),
                        stringResource(R.string.welcome_step_2),
                        stringResource(R.string.welcome_step_3),
                        stringResource(R.string.welcome_step_4)
                    )

                    steps.forEachIndexed { index, string ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text(
                                text = "${index + 1}",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.width(24.dp)
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = string, style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
                Spacer(Modifier.height(32.dp))
                Button(
                    onClick = onImportCalendar
                ) {
                    Text(stringResource(R.string.import_timetable))
                }
                Spacer(Modifier.height(4.dp))
                TextButton(
                    onClick = onAddSemester
                ) {
                    Text(stringResource(R.string.setup_manually))
                }
            }

            courses.isEmpty() -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = onAddCourse
                    ) {
                        Text(stringResource(R.string.add_course))
                    }
                }
            }

            else -> LazyColumn(
                modifier = Modifier.padding(innerPadding),
                contentPadding = PaddingValues(bottom = 88.dp)
            ) {
                item(key = "week") {
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = stringResource(
                                R.string.hours_this_week, formatHours(hoursThisWeek)
                            ),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        if (reachedCourseGoals > 0) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = pluralStringResource(
                                        R.plurals.goals_reached,
                                        reachedCourseGoals,
                                        reachedCourseGoals
                                    ), style = MaterialTheme.typography.bodyMedium
                                )
                                Icon(
                                    painterResource(R.drawable.ic_heart),
                                    contentDescription = stringResource(R.string.goal_reached),
                                    modifier = Modifier.size(16.dp),
                                    tint = PurpleHeart,
                                )

                            }
                        }
                    }
                }
                items(courses, key = { it.id }) { course ->
                    // calculations for first progress bar that has exam date as end date
                    val nextExam =
                        exams.firstOrNull { it.courseId == course.id && it.epochDay >= today }
                    val hoursDone =
                        hoursDonePerCourse(courseId = course.id, sessions = finishedSessions)
                    val courseExam = exams.firstOrNull { it.courseId == course.id }
                    val importedHours =
                        classHoursPerCourse.firstOrNull { it.courseId == course.id }?.classHours
                    val classHours =
                        classHoursForCourse(course, currentSemester.lectureWeeks, importedHours)
                    val totalHours =
                        (course.credits * currentSemester.hoursPerCredit - classHours).coerceAtLeast(
                            0.0
                        )
                    val endDay = courseExam?.epochDay ?: currentSemester.endEpochDay
                    val expected = expectedHours(
                        totalHours = totalHours,
                        endDay = endDay,
                        startDay = currentSemester.startEpochDay,
                        today = today
                    )
                    // calculations for second progress bar that has semester end day as end date
                    val plan = planProgressFor(course, currentSemester, finishedSessions, today)
                    val goalReached = isWeeklyGoalReached(course, finishedSessions)
                    CourseCard(
                        totalHours = totalHours,
                        planTotalHours = plan.planTotalHours,
                        expectedHours = expected,
                        planExpectedHours = plan.planExpectedHours,
                        hoursDone = hoursDone,
                        course = course,
                        hasExam = courseExam != null,
                        nextExam = nextExam,
                        goalReached = goalReached,
                        onClick = { onCourseClick(course.id) })
                }
            }
        }
    }
}
