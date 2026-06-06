package com.nyra.app.android.domain.home

import com.nyra.app.android.core.model.CardIds
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
            cards += catalog.getById(CardIds.CheckInDefault)
        }

        when (lastCheckIn?.moodCode) {
            MoodCode.Tired,
            MoodCode.LowEnergy -> {
                cards += catalog.getById(CardIds.ReflectionTired)
                cards += catalog.getById(CardIds.GentleActionTired)
            }

            MoodCode.Anxious,
            MoodCode.Restless -> {
                cards += catalog.getById(CardIds.ReflectionGrounding)
                cards += catalog.getById(CardIds.BreathingDefault)
            }

            MoodCode.Calm,
            MoodCode.Clear -> {
                cards += catalog.getById(CardIds.ReflectionCalm)
                cards += catalog.getById(CardIds.JournalPromptDefault)
            }

            MoodCode.Open -> {
                cards += catalog.getById(CardIds.ReflectionOpen)
                cards += catalog.getById(CardIds.JournalPromptDefault)
            }

            MoodCode.Quiet -> {
                cards += catalog.getById(CardIds.ReflectionQuiet)
                cards += catalog.getById(CardIds.JournalPromptQuiet)
            }

            null -> {
                cards += catalog.getById(CardIds.ReflectionNeutral)
                cards += catalog.getById(CardIds.BreathingDefault)
            }
            else -> {}
        }

        if (timeOfDay == TimeOfDay.Evening || timeOfDay == TimeOfDay.Night) {
            cards += catalog.getById(CardIds.EveningRelease)
        }

        return cards
            .distinctBy { it.id }
            .filter { it.isActive }
            .sortedByDescending { it.priority }
            .take(4)
    }
}