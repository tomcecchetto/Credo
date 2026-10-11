package com.tom.romcom

import CredoApp
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.tom.romcom.ui.MainArea
import com.tom.romcom.ui.theme.CredoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CredoTheme{
                CredoApp()
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