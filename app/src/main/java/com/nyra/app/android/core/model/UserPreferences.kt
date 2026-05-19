package com.nyra.app.android.core.model

data class UserPreferences(
    val toneStyle: ToneStyle = ToneStyle.Soft,
    val defaultRhythm: ReflectionRhythm = ReflectionRhythm.Evening,
    val atmosphereIntensity: Int = 70,
    val motionEnabled: Boolean = true,
    val hapticsEnabled: Boolean = true,
    val notificationsEnabled: Boolean = false,
    val onboardingCompleted: Boolean = false
)

enum class ToneStyle {
    Soft,
    Neutral,
    Quiet
}

enum class ReflectionRhythm {
    Morning,
    Afternoon,
    Evening,
    LateNight
}