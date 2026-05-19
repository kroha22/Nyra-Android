package com.nyra.app.android.core.catalog

import com.nyra.app.android.core.model.*

object NyraContentCatalog {
    val moodStates = listOf(
        MoodState(
            id = "calm",
            code = MoodCode.Calm,
            title = "Calm",
            description = "A sense of peace and tranquility.",
            valence = 8,
            energy = 3,
            intensity = 2,
            presenceCode = PresenceCode.CalmPresence
        ),
        MoodState(
            id = "open",
            code = MoodCode.Open,
            title = "Open",
            description = "Feeling receptive and connected.",
            valence = 9,
            energy = 6,
            intensity = 4,
            presenceCode = PresenceCode.OpenPresence
        ),
        MoodState(
            id = "tired",
            code = MoodCode.Tired,
            title = "Tired",
            description = "Low energy and needing rest.",
            valence = 4,
            energy = 1,
            intensity = 3,
            presenceCode = PresenceCode.DrainedPresence
        ),
        MoodState(
            id = "anxious",
            code = MoodCode.Anxious,
            title = "Anxious",
            description = "Feeling unsettled or worried.",
            valence = 2,
            energy = 7,
            intensity = 8,
            presenceCode = PresenceCode.RestlessPresence
        )
    )

    val presenceStates = listOf(
        PresenceState(
            id = "calm_presence",
            code = PresenceCode.CalmPresence,
            title = "Calm Presence",
            description = "Stillness and gentle awareness.",
            shapeToken = "circle_soft",
            glowToken = "blue_mist",
            textureToken = "silk",
            motionToken = "slow_pulse",
            backgroundToken = "dawn_haze",
            brightness = 40,
            density = 30,
            expansion = 50,
            warmth = 60
        ),
        PresenceState(
            id = "open_presence",
            code = PresenceCode.OpenPresence,
            title = "Open Presence",
            description = "Expansive and clear awareness.",
            shapeToken = "star_diffused",
            glowToken = "golden_aura",
            textureToken = "air",
            motionToken = "radiate",
            backgroundToken = "clear_sky",
            brightness = 80,
            density = 20,
            expansion = 90,
            warmth = 80
        )
    )

    val cardTemplates = listOf(
        CardTemplate(
            id = "morning_checkin",
            type = CardType.CheckIn,
            title = "Morning Check-in",
            subtitle = "How are you starting your day?",
            iconToken = "ic_morning",
            action = CardAction(CardActionType.OpenCheckIn),
            priority = 100
        ),
        CardTemplate(
            id = "daily_reflection",
            type = CardType.Reflection,
            title = "Daily Reflection",
            subtitle = "Take a moment to look back.",
            iconToken = "ic_journal",
            action = CardAction(CardActionType.OpenJournal),
            priority = 80
        ),
        CardTemplate(
            id = "gentle_pause",
            type = CardType.Presence,
            title = "Gentle Pause",
            subtitle = "Just be here for a minute.",
            iconToken = "ic_pause",
            action = CardAction(CardActionType.OpenPause),
            priority = 90
        )
    )

    val gentleActions = listOf(
        GentleAction(
            id = "breath_count",
            title = "Breath Counting",
            description = "Simple 4-7-8 breathing for grounding.",
            durationSeconds = 120,
            category = GentleActionCategory.Breath,
            suitableMoodCodes = listOf(MoodCode.Anxious, MoodCode.Restless),
            timeOfDay = listOf(TimeOfDay.Morning, TimeOfDay.Evening, TimeOfDay.Night),
            intensity = GentleActionIntensity.Low
        ),
        GentleAction(
            id = "short_walk",
            title = "Mindful Walk",
            description = "A short walk paying attention to each step.",
            durationSeconds = 600,
            category = GentleActionCategory.Outside,
            suitableMoodCodes = listOf(MoodCode.Tired, MoodCode.LowEnergy),
            timeOfDay = listOf(TimeOfDay.Afternoon),
            intensity = GentleActionIntensity.Medium
        ),
        GentleAction(
            id = "glass_water",
            title = "Sip of Water",
            description = "Drink a glass of water mindfully.",
            durationSeconds = 60,
            category = GentleActionCategory.Water,
            suitableMoodCodes = MoodCode.entries.toList(),
            timeOfDay = TimeOfDay.entries.toList(),
            intensity = GentleActionIntensity.Low
        )
    )
}