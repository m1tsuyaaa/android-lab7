package com.example.mycity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import com.example.mycity.ui.MyCityApp
import com.example.mycity.ui.theme.MyCityTheme

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activity = this
        setContent {
            val windowSizeClass = calculateWindowSizeClass(activity)
            MyCityTheme {
                MyCityApp(windowSizeClass = windowSizeClass)
            }
        }
    }
}
