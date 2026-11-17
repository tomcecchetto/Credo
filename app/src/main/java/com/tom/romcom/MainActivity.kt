package com.tom.romcom


import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.tom.romcom.ui.CredoApp
import com.tom.romcom.ui.Tab
import com.tom.romcom.ui.theme.CredoTheme

const val START_TAB_EXTRA = "start_tab"


class MainActivity : ComponentActivity() {
    var tabRequest by mutableStateOf<Tab?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val startTabName = intent.getStringExtra(START_TAB_EXTRA)
        val startTab = Tab.entries.find { it.name == startTabName } ?: Tab.PROGRESS
        enableEdgeToEdge()
        setContent {
            CredoTheme{
                CredoApp(
                    startTab = startTab,
                    tabRequest = tabRequest,
                    onTabRequestHandled = { tabRequest = null }
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val requestedTabName = intent.getStringExtra(START_TAB_EXTRA)
        val requestedTab = Tab.entries.find { it.name == requestedTabName }
        tabRequest = requestedTab
    }
}



@Preview(showBackground = true)
@Composable
fun CredoAppPreview() {
    CredoTheme {
        CredoApp()
    }
}
