package com.tom.romcom.ui

import com.tom.romcom.data.Course

val courseColors = listOf(
    0xFFFF0000,
    0xFFFFFF00,
    0xFF008000,
    0xFF0000FF,
    0xFF800080,
    0xFFFF00FF,
    0xFF00FFFF,
    0xFF00FF00,
    0xFF808000,
    0xFFC0C0C0
)
fun nextFreeColor(existing: List<Course>): Long {
    return courseColors.firstOrNull { color -> existing.none { it.color == color } } ?: courseColors.first()
}
