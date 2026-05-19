package com.nyra.app.android.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nyra.app.android.core.designsystem.theme.NyraBackground
import com.nyra.app.android.core.designsystem.theme.NyraCard
import com.nyra.app.android.core.designsystem.theme.NyraPresenceView
import com.nyra.app.android.core.designsystem.theme.NyraSpacing

@Composable
fun HomeRoute(
    onOpenCheckIn: () -> Unit,
    onOpenJournal: () -> Unit,
    onOpenPause: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeScreen(
        uiState = uiState,
        onOpenCheckIn = onOpenCheckIn,
        onOpenJournal = onOpenJournal,
        onOpenPause = onOpenPause
    )
}

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onOpenCheckIn: () -> Unit,
    onOpenJournal: () -> Unit,
    onOpenPause: () -> Unit
) {
    NyraBackground {
        LazyColumn(
            modifier = Modifier.padding(horizontal = NyraSpacing.lg),
            contentPadding = PaddingValues(
                top = NyraSpacing.xl,
                bottom = NyraSpacing.xxl
            ),
            verticalArrangement = Arrangement.spacedBy(NyraSpacing.md)
        ) {
            item {
                Column {
                    Text(text = uiState.greeting)

                    uiState.subtitle?.let {
                        Spacer(modifier = Modifier.height(NyraSpacing.xs))
                        Text(text = it)
                    }
                }
            }

            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    NyraPresenceView()
                }
            }

            items(uiState.cards) { card ->
                NyraCard {
                    Column {
                        Text(text = card.title)

                        card.subtitle?.let {
                            Spacer(modifier = Modifier.height(NyraSpacing.xs))
                            Text(text = it)
                        }
                    }
                }
            }
        }
    }
}