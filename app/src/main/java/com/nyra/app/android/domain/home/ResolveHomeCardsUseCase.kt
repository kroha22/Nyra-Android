package com.nyra.app.android.domain.home

import com.nyra.app.android.core.model.CardTemplate
import com.nyra.app.android.core.model.CardType
import com.nyra.app.android.core.model.CheckInEntry
import com.nyra.app.android.core.model.MoodCode
import com.nyra.app.android.core.model.TimeOfDay
import com.nyra.app.android.data.cards.NyraCardCatalog
import javax.inject.Inject

class ResolveHomeCardsUseCase @Inject constructor(
    private val catalog: NyraCardCatalog
) {
    operator fun invoke(
        lastCheckIn: CheckInEntry?,
        hasCheckedInToday: Boolean,
        timeOfDay: TimeOfDay
    ): List<CardTemplate> {
        val cards = mutableListOf<CardTemplate>()

        if (!hasCheckedInToday) {
            cards += catalog.getByType(CardType.CheckIn)
        }

        when (lastCheckIn?.moodCode) {
            MoodCode.Tired,
            MoodCode.LowEnergy -> {
                cards += catalog.getById("reflection_tired_evening")
                cards += catalog.getById("action_rest_shoulders")
            }

            MoodCode.Anxious,
            MoodCode.Restless -> {
                cards += catalog.getById("reflection_grounding")
                cards += catalog.getById("breathing_soft_pause")
            }

            MoodCode.Calm,
            MoodCode.Clear -> {
                cards += catalog.getById("reflection_calm")
                cards += catalog.getById("journal_soft_prompt")
            }

            MoodCode.Open -> {
                cards += catalog.getById("reflection_open")
                cards += catalog.getById("journal_intention")
            }

            MoodCode.Quiet -> {
                cards += catalog.getById("reflection_quiet")
                cards += catalog.getById("action_slow_evening")
            }

            null -> {
                cards += catalog.getById("reflection_neutral")
                cards += catalog.getById("breathing_soft_pause")
            }
            else -> {}
        }

        if (timeOfDay == TimeOfDay.Evening || timeOfDay == TimeOfDay.Night) {
            cards += catalog.getById("evening_release")
        }

        return cards
            .distinctBy { it.id }
            .filter { it.isActive }
            .sortedByDescending { it.priority }
            .take(4)
    }
}