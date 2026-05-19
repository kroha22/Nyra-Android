package com.nyra.app.android.core.model

data class MoodState(
    val id: String,
    val code: MoodCode,
    val title: String,
    val description: String? = null,
    val valence: Int,
    val energy: Int,
    val intensity: Int,
    val presenceCode: PresenceCode,
    val isActive: Boolean = true
)

enum class MoodCode {
    Calm,
    Tired,
    Anxious,
    Open,
    LowEnergy,
    Quiet,
    Clear,
    Restless
}