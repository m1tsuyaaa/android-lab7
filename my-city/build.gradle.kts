// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.5.2" apply false
    id("org.jetbrains.kotlin.android") version "2.0.20" apply false
    // With Kotlin 2.x the Compose compiler is applied as its own Gradle plugin.
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.20" apply false
}
