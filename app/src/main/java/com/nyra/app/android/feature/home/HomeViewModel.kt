package com.nyra.app.android.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nyra.app.android.domain.home.ObserveHomeStateUseCase
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
            HomeUiState(
                isLoading = false,
                greeting = homeState.greeting,
                subtitle = homeState.subtitle,
                presenceState = homeState.presenceState,
                cards = homeState.cards,
                hasCheckedInToday = homeState.hasCheckedInToday
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeUiState()
        )
}