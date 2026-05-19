package com.nyra.app.android.core.model

data class CardTemplate(
    val id: String,
    val type: CardType,
    val title: String,
    val subtitle: String? = null,
    val iconToken: String? = null,
    val visualToken: String? = null,
    val action: CardAction,
    val priority: Int,
    val isActive: Boolean = true
)

enum class CardType {
    CheckIn,
    Reflection,
    GentleAction,
    Breathing,
    JournalPrompt,
    Presence
}

data class CardAction(
    val type: CardActionType,
    val payload: Map<String, String> = emptyMap()
)

enum class CardActionType {
    OpenCheckIn,
    OpenJournal,
    OpenPause,
    OpenInsight,
    ShowAction,
    None
}