package com.nyra.app.android.core.ui_state.runtime

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nyra.app.android.core.profile.model.NyraHomeStateLevel
import com.nyra.app.android.core.profile.model.NyraUserProfile
import com.nyra.app.android.core.ui_state.composition.HomeUiCompositionResolver
import com.nyra.app.android.core.ui_state.model.NyraUiStateConfig
import com.nyra.app.android.core.ui_state.resolver.NyraNocturnePalette
import com.nyra.app.android.core.ui_state.resolver.NyraUiStateConfigResolver
import com.nyra.app.android.core.ui_state.runtime.modules.ArchetypePreviewCard
import com.nyra.app.android.core.ui_state.runtime.modules.DailyEmotionalAtmosphereHero
import com.nyra.app.android.core.ui_state.runtime.modules.EmotionalCheckInOrbs
import com.nyra.app.android.core.ui_state.runtime.modules.EmotionalClimateCard
import com.nyra.app.android.core.ui_state.runtime.modules.EmotionalRhythmCard
import com.nyra.app.android.core.ui_state.runtime.modules.HeroPersonalizationCard
import com.nyra.app.android.core.ui_state.runtime.modules.IdentityPreviewCard
import com.nyra.app.android.core.ui_state.runtime.modules.MemoryEchoCard
import com.nyra.app.android.core.ui_state.runtime.modules.MoodTimelinePlaceholder
import com.nyra.app.android.core.ui_state.runtime.modules.PatternHintCard
import com.nyra.app.android.core.ui_state.runtime.modules.ReflectionPromptCard
import com.nyra.app.android.core.ui_state.runtime.modules.TrackerQuickAdd
import com.nyra.app.android.core.ui_state.runtime.modules.argbToColor

/**
 * Adaptive Home Screen — the cinematic Warm Horizon surface.
 *
 * Composition is driven by [stateLevel] (the spec's "States 0–5"). Each higher
 * level keeps the modules of the prior one and adds its own — Nyra "remembers
 * how far it's gone" with the user without ever resetting context.
 *
 * Background layers come from [EmotionalAtmosphereBackground], whose ribbon /
 * topology overlays unlock at exactly the same level thresholds.
 */
@Composable
fun AdaptiveHomeScreen(
    profile: NyraUserProfile,
    stateLevel: NyraHomeStateLevel,
    uiStateResolver: NyraUiStateConfigResolver,
    compositionResolver: HomeUiCompositionResolver,
    onBeginPersonalization: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val uiState = rememberNyraUiState(profile = profile, resolver = uiStateResolver)
    val composition = compositionResolver.resolve(profile = profile, uiState = uiState)

    EmotionalAtmosphereBackground(
        uiState = uiState,
        level = stateLevel,
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 28.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            HeaderRow(title = composition.atmosphericTitle ?: "Nyra")

            Spacer(modifier = Modifier.height(12.dp))

            HomeModules(
                profile = profile,
                stateLevel = stateLevel,
                uiState = uiState,
                topPrompt = composition.topPrompt,
                onBeginPersonalization = onBeginPersonalization
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun HeaderRow(title: String) {
    Text(
        text = title,
        color = NyraNocturnePalette.TextSecondary.argbToColor(),
        fontSize = 12.sp,
        fontWeight = FontWeight.Light,
        letterSpacing = 4.sp,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun HomeModules(
    profile: NyraUserProfile,
    stateLevel: NyraHomeStateLevel,
    uiState: NyraUiStateConfig,
    topPrompt: String?,
    onBeginPersonalization: () -> Unit
) {
    val prompt = topPrompt ?: defaultPromptFor(stateLevel)

    // ─── State 0 — Empty Home ────────────────────────────────────────────────
    if (stateLevel == NyraHomeStateLevel.Empty) {
        HeroPersonalizationCard(onBegin = onBeginPersonalization)
        EmotionalCheckInOrbs(onSelect = { /* wire to check-in repo later */ })
        ReflectionPromptCard(prompt = prompt, onTap = { /* open prompt editor */ })
        MoodTimelinePlaceholder()
        return
    }

    // ─── State 2+ Hero (only when natal data present) ────────────────────────
    if (stateLevel.atLeast(NyraHomeStateLevel.NatalEnabled)) {
        DailyEmotionalAtmosphereHero(
            headline = atmosphereHeadlineFor(stateLevel),
            subtitle = atmosphereSubtitleFor(stateLevel)
        )
    } else {
        // State 1 still uses the personalization hero, just paired with rhythm.
        HeroPersonalizationCard(onBegin = onBeginPersonalization)
    }

    EmotionalCheckInOrbs(onSelect = { /* check-in repo */ })

    // ─── State 1 — Trackers enabled ──────────────────────────────────────────
    if (stateLevel.atLeast(NyraHomeStateLevel.TrackersEnabled)) {
        EmotionalRhythmCard(
            headline = "You've felt more mentally scattered this week."
        )
        PatternHintCard(
            hint = "Socially intense days seem to drain your emotional energy."
        )
        TrackerQuickAdd(
            trackers = listOf(
                "overstimulation",
                "sleep quality",
                "inspiration",
                "social energy"
            ),
            onAdd = { /* tracker repo */ }
        )
    }

    // ─── State 2 — Natal enabled ─────────────────────────────────────────────
    if (stateLevel.atLeast(NyraHomeStateLevel.NatalEnabled)) {
        IdentityPreviewCard(profile = profile)
        ArchetypePreviewCard(profile = profile)
    }

    // ─── State 3 — Transits active ───────────────────────────────────────────
    if (stateLevel.atLeast(NyraHomeStateLevel.TransitsActive)) {
        EmotionalClimateCard(
            headline = "Emotional processing may feel slower today.",
            secondary = "You may benefit from quieter environments."
        )
    }

    // ─── State 4 — History accumulated ───────────────────────────────────────
    if (stateLevel.atLeast(NyraHomeStateLevel.HistoryAccumulated)) {
        MemoryEchoCard(
            body = "You reflected on emotional exhaustion several times recently."
        )
    }

    // ─── State 5 — Multi-system ──────────────────────────────────────────────
    if (stateLevel.atLeast(NyraHomeStateLevel.MultiSystem)) {
        MemoryEchoCard(
            body = "You seek closeness while needing strong emotional autonomy."
        )
    }

    ReflectionPromptCard(prompt = prompt, onTap = { /* open prompt editor */ })
}

private fun defaultPromptFor(level: NyraHomeStateLevel): String = when (level) {
    NyraHomeStateLevel.Empty -> "What emotionally affected you today?"
    NyraHomeStateLevel.TrackersEnabled -> "What pulled the most energy from you this week?"
    NyraHomeStateLevel.NatalEnabled -> "What has emotionally overloaded you recently?"
    NyraHomeStateLevel.TransitsActive -> "Have you felt emotionally overstimulated lately?"
    NyraHomeStateLevel.HistoryAccumulated -> "What pattern keeps returning?"
    NyraHomeStateLevel.MultiSystem -> "Where do your needs feel in tension today?"
}

private fun atmosphereHeadlineFor(level: NyraHomeStateLevel): String = when (level) {
    NyraHomeStateLevel.NatalEnabled,
    NyraHomeStateLevel.TransitsActive -> "You may need more internal space today."
    NyraHomeStateLevel.HistoryAccumulated -> "Your emotional energy has been quieter this month."
    NyraHomeStateLevel.MultiSystem -> "Your inner systems are leaning toward solitude."
    else -> "Nyra is reading your atmosphere."
}

private fun atmosphereSubtitleFor(level: NyraHomeStateLevel): String = when (level) {
    NyraHomeStateLevel.NatalEnabled -> "Emotional sensitivity feels heightened lately."
    NyraHomeStateLevel.TransitsActive -> "Social energy feels less stable today."
    NyraHomeStateLevel.HistoryAccumulated -> "You often seek solitude after emotionally intense days."
    NyraHomeStateLevel.MultiSystem -> "You appear open while protecting deeper vulnerability."
    else -> ""
}
