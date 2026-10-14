package com.tom.romcom.ui.progress

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tom.romcom.R
import com.tom.romcom.ui.CredoTopBar
import com.tom.romcom.ui.course.CourseCard

@Composable
fun ProgressScreen(
    onAddCourse: () -> Unit,
    onAddSemester: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProgressViewModel = viewModel()

) {
    val courses by viewModel.courses.collectAsState(initial = emptyList())
    val semester by viewModel.semester.collectAsState(initial = null)

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = { CredoTopBar(stringResource(R.string.progress)) },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            if (semester != null) {
                ExtendedFloatingActionButton(onClick = onAddCourse) {
                    Icon(painterResource(R.drawable.ic_add_course), contentDescription = stringResource(R.string.add_course))
                }
            }
        }
    ) { innerPadding ->
        when {
            semester == null ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = onAddSemester
                    ) {
                        Text(stringResource(R.string.add_semester))
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
                modifier = Modifier
                    .padding(innerPadding)
            ) {
                items(courses, key = { it.id }) { course ->
                    CourseCard(course = course)
                }
            }
        }
    }
}