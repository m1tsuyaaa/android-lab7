package com.example.cupcake.test

import android.graphics.Bitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import com.example.cupcake.CupcakeApp
import com.example.cupcake.R
import org.junit.Rule
import org.junit.Test
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * Captures the main Cupcake screens for the lab report.
 */
class ScreenshotCaptureTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<androidx.activity.ComponentActivity>()

    @Test
    fun captureCupcakeScreens() {
        composeTestRule.setContent { CupcakeApp() }
        composeTestRule.waitForIdle()
        save("cupcake_01_start")

        composeTestRule.onNodeWithStringId(R.string.one_cupcake).performClick()
        composeTestRule.onNodeWithStringId(R.string.chocolate).performClick()
        composeTestRule.waitForIdle()
        save("cupcake_02_flavor")

        composeTestRule.onNodeWithStringId(R.string.next).performClick()
        composeTestRule.waitForIdle()
        save("cupcake_03_pickup")

        composeTestRule.onNodeWithText(formattedTomorrow()).performClick()
        composeTestRule.onNodeWithStringId(R.string.next).performClick()
        composeTestRule.waitForIdle()
        save("cupcake_04_summary")
    }

    private fun formattedTomorrow(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, 1)
        return SimpleDateFormat("E MMM d", Locale.getDefault()).format(calendar.time)
    }

    private fun save(name: String) {
        val bitmap = composeTestRule.onRoot().captureToImage().asAndroidBitmap()
        val dir = File(
            InstrumentationRegistry.getInstrumentation().targetContext.filesDir,
            "screenshots"
        )
        dir.mkdirs()
        FileOutputStream(File(dir, "$name.png")).use {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
        }
    }
}
