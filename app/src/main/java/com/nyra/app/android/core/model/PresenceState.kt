package com.nyra.app.android.core.model

data class PresenceState(
    val id: String,
    val code: PresenceCode,
    val title: String,
    val description: String? = null,
    val shapeToken: String,
    val glowToken: String,
    val textureToken: String,
    val motionToken: String,
    val backgroundToken: String,
    val brightness: Int,
    val density: Int,
    val expansion: Int,
    val warmth: Int
)

enum class PresenceCode {
    CalmPresence,
    GroundedPresence,
    QuietPresence,
    OpenPresence,
    DrainedPresence,
    RestlessPresence,
    NeutralPresence
}