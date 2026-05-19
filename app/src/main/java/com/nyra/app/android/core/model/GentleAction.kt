package com.nyra.app.android.core.model

data class GentleAction(
    val id: String,
    val title: String,
    val description: String? = null,
    val durationSeconds: Int? = null,
    val category: GentleActionCategory,
    val suitableMoodCodes: List<MoodCode> = emptyList(),
    val unsuitableMoodCodes: List<MoodCode> = emptyList(),
    val timeOfDay: List<TimeOfDay> = emptyList(),
    val intensity: GentleActionIntensity,
    val isActive: Boolean = true
)

enum class GentleActionCategory {
    Body,
    Breath,
    Rest,
    Outside,
    Water,
    Reflection
}

enum class GentleActionIntensity {
    Low,
    Medium
}