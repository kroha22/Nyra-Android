package com.nyra.app.android.feature.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nyra.app.android.core.model.CardAction
import com.nyra.app.android.core.model.CardActionType

/**
 * Legacy non-adaptive Home route.
 *
 * The previous implementation called `HomeMockup` (a hand-painted Canvas mockup
 * with Compose API drift that no longer compiles); that file has been deleted.
 * The adaptive Home is now reachable via `core/ui_state/runtime/AdaptiveHomeRoute`
 * and is the one wired into `MainActivity`.
 *
 * This composable is kept as a thin stub so the Hilt ViewModel + action dispatch
 * scaffolding stays validated by the compiler. It renders a minimal placeholder
 * and is not referenced by Navigation today. Delete the file or rebuild the body
 * when a non-adaptive variant is needed again.
 */
@Composable
fun HomeRoute(
    onOpenCheckIn: () -> Unit,
    onOpenJournal: () -> Unit,
    onOpenPause: () -> Unit,
    onOpenInsights: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    @Suppress("UNUSED_VARIABLE")
    val handleCardAction: (CardAction) -> Unit = { action ->
        when (action.type) {
            CardActionType.OpenCheckIn -> onOpenCheckIn()
            CardActionType.OpenJournal -> onOpenJournal()
            CardActionType.OpenPause -> onOpenPause()
            CardActionType.OpenInsight -> onOpenInsights()
            CardActionType.ShowAction -> Unit
            CardActionType.None -> Unit
        }
    }

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = uiState.greeting.ifBlank { "Home (stub)" })
    }
}
