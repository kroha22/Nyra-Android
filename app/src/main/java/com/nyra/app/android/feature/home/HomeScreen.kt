package com.nyra.app.android.feature.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nyra.app.android.core.model.CardAction
import com.nyra.app.android.core.model.CardActionType
import com.nyra.app.android.core.model.PresenceCode
import com.nyra.app.android.feature.home.model.AtmosphereState

@Composable
fun HomeRoute(
    onOpenCheckIn: () -> Unit,
    onOpenJournal: () -> Unit,
    onOpenPause: () -> Unit,
    onOpenInsights: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val handleCardAction: (CardAction) -> Unit = { action ->
        when (action.type) {
            CardActionType.OpenCheckIn -> onOpenCheckIn()
            CardActionType.OpenJournal -> onOpenJournal()
            CardActionType.OpenPause -> onOpenPause()
            CardActionType.OpenInsight -> onOpenInsights()
            CardActionType.ShowAction -> {
                // TODO: Show bottom sheet or snackbar later
            }
            CardActionType.None -> Unit
        }
    }

    // Map Domain TimeOfDay or Presence to AtmosphereState if needed, 
    // for now using default EVENING as in system description or deriving from state
    val atmosphere = when (uiState.presenceState?.code) {
        PresenceCode.DrainedPresence -> AtmosphereState.TIRED
        PresenceCode.RestlessPresence -> AtmosphereState.RESTLESS
        PresenceCode.CalmPresence -> AtmosphereState.CALM
        PresenceCode.GroundedPresence -> AtmosphereState.GROUNDED
        else -> AtmosphereState.EVENING
    }

    HomeMockup(
        atmosphere = atmosphere,
        presenceCode = uiState.presenceState?.code ?: PresenceCode.NeutralPresence,
        cards = uiState.cards,
        greeting = uiState.greeting,
        subtitle = uiState.subtitle ?: "",
        onCardClick = handleCardAction
    )
}
