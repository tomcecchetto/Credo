package com.tom.romcom.ui

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object MainAreaRoute : NavKey

@Serializable
data object SemesterSetupRoute : NavKey

@Serializable
data class CourseSetupRoute(val courseId: Int? = null) : NavKey

@Serializable
data class SessionEditRoute(val sessionId: Int? = null) : NavKey

