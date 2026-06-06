package com.nyra.app.android.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nyra.app.android.core.model.CardType
import com.nyra.app.android.core.model.MoodCode
import com.nyra.app.android.core.model.TimeOfDay
import com.nyra.app.android.domain.home.ObserveHomeStateUseCase
import com.nyra.app.android.feature.home.mapper.toHomeCardUiModel
import com.nyra.app.android.feature.home.model.AtmosphereState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    observeHomeState: ObserveHomeStateUseCase
) : ViewModel() {

    val uiState = observeHomeState()
        .map { homeState ->
            // Resolve AtmosphereState based on Mood or TimeOfDay
            val atmosphere = when (homeState.lastCheckIn?.moodCode) {
                MoodCode.Tired, MoodCode.LowEnergy -> AtmosphereState.TIRED
                MoodCode.Anxious, MoodCode.Restless -> AtmosphereState.RESTLESS
                MoodCode.Calm, MoodCode.Clear -> AtmosphereState.CALM
                MoodCode.Quiet -> AtmosphereState.GROUNDED
                else -> when (homeState.timeOfDay) {
                    TimeOfDay.Morning -> AtmosphereState.MORNING
                    TimeOfDay.Afternoon -> AtmosphereState.AFTERNOON
                    TimeOfDay.Evening -> AtmosphereState.EVENING
                    TimeOfDay.Night -> AtmosphereState.NIGHT
                }
            }

            HomeUiState(
                isLoading = false,
                greeting = homeState.greeting,
                subtitle = homeState.subtitle,
                atmosphere = atmosphere,
                presenceState = homeState.presenceState,
                cards = homeState.cards.map { card ->
                    card.toHomeCardUiModel(
                        isHighlighted = card.type == CardType.CheckIn &&
                            !homeState.hasCheckedInToday
                    )
                },
                hasCheckedInToday = homeState.hasCheckedInToday
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeUiState()
        )
}