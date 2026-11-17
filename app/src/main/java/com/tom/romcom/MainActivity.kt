package com.tom.romcom


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.tom.romcom.ui.CredoApp
import com.tom.romcom.ui.Tab
import com.tom.romcom.ui.theme.CredoTheme

const val START_TAB_EXTRA = "start_tab"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val startTabName = intent.getStringExtra(START_TAB_EXTRA)
        val startTab = Tab.entries.find { it.name == startTabName } ?: Tab.PROGRESS
        enableEdgeToEdge()
        setContent {
            CredoTheme{
                CredoApp(startTab = startTab)
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun CredoAppPreview() {
    CredoTheme {
        CredoApp()
    }
}
