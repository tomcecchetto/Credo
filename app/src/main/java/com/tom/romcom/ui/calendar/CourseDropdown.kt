package com.tom.romcom.ui.calendar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.tom.romcom.R
import com.tom.romcom.data.Course

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseDropdown(
    courses: List<Course>,
    suggestedName: String,
    selectedCourseId: Int?,
    onCourseSelected: (Int?) -> Unit,
    onCreateCourse: (String, Double) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var name by rememberSaveable { mutableStateOf(suggestedName) }
    var credits by rememberSaveable { mutableStateOf("") }

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
            DropdownMenuItem(
                text = { Text(stringResource(R.string.new_course)) },
                onClick = {
                    showDialog = true
                    expanded = false
                    name = suggestedName
                    credits = ""
                }
            )
        }
        if (showDialog) {
            val creditsValue = credits.replace(',', '.').toDoubleOrNull()

            AlertDialog(
                title = {
                    Text(
                        text = stringResource(R.string.add_new_course),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Column {
                        OutlinedTextField(
                            label = { Text(stringResource(R.string.course_name)) },
                            value = name,
                            onValueChange = { name = it },
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Sentences,
                                imeAction = ImeAction.Next
                            ),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        )
                        OutlinedTextField(
                            label = { Text(stringResource(R.string.credits)) },
                            value = credits,
                            onValueChange = { credits = it },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Decimal,
                                imeAction = ImeAction.Done
                            ),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)

                        )
                    }
                },
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            creditsValue?.let { onCreateCourse(name.trim(), it) }
                            showDialog = false
                        },
                        enabled = name.isNotBlank() && creditsValue != null && creditsValue > 0.0
                    ) {
                        Text(stringResource(R.string.add_course))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text(stringResource(R.string.cancel))
                    }
                }
            )
        }

    }
}
