package com.tom.romcom.ui.calendar

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.tom.romcom.R
import com.tom.romcom.data.Course

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseDropdown(
    courses: List<Course>,
    selectedCourseId: Int?,
    onCourseSelected: (Int?) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        val courseName = courses.firstOrNull { it.id == selectedCourseId }?.name
        OutlinedTextField(
            value = courseName ?: stringResource(R.string.no_course),
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource(R.string.course_name)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable)
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.no_course)) },
                onClick = {
                    onCourseSelected(null)
                    expanded = false
                }
            )
            courses.forEach { course ->
                DropdownMenuItem(
                    text = { Text(course.name) },
                    onClick = {
                        onCourseSelected(course.id)
                        expanded = false
                    }
                )
            }
        }
    }
}
