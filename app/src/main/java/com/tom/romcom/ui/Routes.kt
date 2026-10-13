package com.tom.romcom.ui

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object MainAreaRoute : NavKey

@Serializable
data object SemesterSetupRoute : NavKey

@Serializable
data object CourseSetupRoute : NavKey