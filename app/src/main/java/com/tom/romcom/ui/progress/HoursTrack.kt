package com.tom.romcom.ui.progress

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun HoursTrack(
    totalHours: Double,
    expectedHours: Double,
    hoursDone: Double,
    modifier: Modifier = Modifier
) {
    val behind = MaterialTheme.colorScheme.error
    val ahead = Color(0xFF2E7D32)
    val trackColor = MaterialTheme.colorScheme.outlineVariant
    val expectedColor = MaterialTheme.colorScheme.outline

    if (totalHours <= 0) return

    val doneFraction = (hoursDone / totalHours).coerceIn(0.0, 1.0).toFloat()
    val expectedFraction = (expectedHours / totalHours).coerceIn(0.0, 1.0).toFloat()

    val doneColor = if (hoursDone >= expectedHours) ahead else behind

    Canvas(modifier = modifier.fillMaxWidth().height(16.dp)) {
        val y = size.height / 2
        drawLine(
            color = trackColor,
            start = Offset(0f, y),
            end = Offset(size.width, y),
            strokeWidth = 4.dp.toPx()
        )
        drawCircle(
            color = expectedColor,
            radius = 6.dp.toPx(),
            center = Offset(size.width * expectedFraction, y)
        )
        drawCircle(
            color = doneColor,
            radius = 6.dp.toPx(),
            center = Offset(size.width * doneFraction, y)
        )
    }
}