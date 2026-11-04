package com.tom.romcom.widget

import android.content.Context
import androidx.glance.appwidget.updateAll

suspend fun updateAllWidgets(context: Context) {
    CredoWidget().updateAll(context)
    TodayWidget().updateAll(context)
}