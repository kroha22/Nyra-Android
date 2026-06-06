package com.nyra.app.android.data.cards

import com.nyra.app.android.core.model.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NyraCardCatalog @Inject constructor() {
    
    private val templates = listOf(
        CardTemplate(
            id = CardIds.CheckInDefault,
            type = CardType.CheckIn,
            title = "How are you feeling?",
            subtitle = "Check in with yourself and name what’s here.",
            iconToken = IconToken.Heart,
            action = CardAction(CardActionType.OpenCheckIn),
            priority = 100
        ),
        CardTemplate(
            id = CardIds.ReflectionMorning,
            type = CardType.Reflection,
            title = "Morning reflection",
            subtitle = "Begin your day with a quiet thought.",
            iconToken = IconToken.Presence,
            action = CardAction(CardActionType.OpenJournal),
            priority = 80
        ),
        CardTemplate(
            id = CardIds.ReflectionAfternoon,
            type = CardType.Reflection,
            title = "Afternoon pause",
            subtitle = "Notice the light and the rhythm of your day.",
            iconToken = IconToken.Presence,
            action = CardAction(CardActionType.OpenJournal),
            priority = 80
        ),
        CardTemplate(
            id = CardIds.ReflectionEvening,
            type = CardType.Reflection,
            title = "Evening reflection",
            subtitle = "Look back on your day with kindness.",
            iconToken = IconToken.Presence,
            action = CardAction(CardActionType.OpenJournal),
            priority = 80
        ),
        CardTemplate(
            id = CardIds.ReflectionNight,
            type = CardType.Reflection,
            title = "Nightly stillness",
            subtitle = "Release the day and prepare for rest.",
            iconToken = IconToken.Presence,
            action = CardAction(CardActionType.OpenJournal),
            priority = 80
        ),
        CardTemplate(
            id = CardIds.ReflectionNeutral,
            type = CardType.Reflection,
            title = "Daily reflection",
            subtitle = "Take a moment to simply observe.",
            iconToken = IconToken.Presence,
            action = CardAction(CardActionType.OpenJournal),
            priority = 80
        ),
        CardTemplate(
            id = CardIds.ReflectionTired,
            type = CardType.Reflection,
            title = "Rest and notice",
            subtitle = "Let the day soften a little.",
            iconToken = IconToken.Presence,
            action = CardAction(CardActionType.OpenJournal),
            priority = 85
        ),
        CardTemplate(
            id = CardIds.ReflectionGrounding,
            type = CardType.Reflection,
            title = "Finding ground",
            subtitle = "Notice what is steady right now.",
            iconToken = IconToken.Presence,
            action = CardAction(CardActionType.OpenJournal),
            priority = 85
        ),
        CardTemplate(
            id = CardIds.ReflectionCalm,
            type = CardType.Reflection,
            title = "Savor the calm",
            subtitle = "Notice the stillness within.",
            iconToken = IconToken.Presence,
            action = CardAction(CardActionType.OpenJournal),
            priority = 80
        ),
        CardTemplate(
            id = CardIds.ReflectionOpen,
            type = CardType.Reflection,
            title = "Expansive heart",
            subtitle = "Notice the space and possibilities.",
            iconToken = IconToken.Presence,
            action = CardAction(CardActionType.OpenJournal),
            priority = 80
        ),
        CardTemplate(
            id = CardIds.ReflectionQuiet,
            type = CardType.Reflection,
            title = "Internal quiet",
            subtitle = "Listen to what silence has to say.",
            iconToken = IconToken.Presence,
            action = CardAction(CardActionType.OpenJournal),
            priority = 80
        ),
        CardTemplate(
            id = CardIds.GentleActionTired,
            type = CardType.GentleAction,
            title = "Small step for you",
            subtitle = "Relax your shoulders for a moment.",
            iconToken = IconToken.Leaf,
            action = CardAction(CardActionType.ShowAction),
            priority = 70
        ),
        CardTemplate(
            id = CardIds.GentleActionRestless,
            type = CardType.GentleAction,
            title = "Gentle move",
            subtitle = "Try a small stretch.",
            iconToken = IconToken.Leaf,
            action = CardAction(CardActionType.ShowAction),
            priority = 70
        ),
        CardTemplate(
            id = CardIds.BreathingDefault,
            type = CardType.Breathing,
            title = "Breathe with Nyra",
            subtitle = "Take a moment to breathe and come back to now.",
            iconToken = IconToken.Wave,
            action = CardAction(CardActionType.OpenPause),
            priority = 90
        ),
        CardTemplate(
            id = CardIds.JournalPromptDefault,
            type = CardType.JournalPrompt,
            title = "Daily journal",
            subtitle = "Capture a thought from today.",
            iconToken = IconToken.Journal,
            action = CardAction(CardActionType.OpenJournal),
            priority = 60
        ),
        CardTemplate(
            id = CardIds.JournalPromptQuiet,
            type = CardType.JournalPrompt,
            title = "Write a word",
            subtitle = "What is one thing you notice now?",
            iconToken = IconToken.Journal,
            action = CardAction(CardActionType.OpenJournal),
            priority = 60
        ),
        CardTemplate(
            id = CardIds.EveningRelease,
            type = CardType.Reflection,
            title = "Evening release",
            subtitle = "You can rest here for a while.",
            iconToken = IconToken.Presence,
            action = CardAction(CardActionType.OpenPause),
            priority = 95
        )
    )

    fun getByType(type: CardType): List<CardTemplate> {
        return templates.filter { it.type == type }
    }

    fun getById(id: String): CardTemplate {
        return templates.find { it.id == id }
            ?: templates.first { it.id == CardIds.CheckInDefault }
    }
}
