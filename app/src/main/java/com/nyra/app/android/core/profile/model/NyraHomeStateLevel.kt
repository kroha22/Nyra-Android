package com.nyra.app.android.core.profile.model

/**
 * Adaptive Home Screen state level — the spec's "States 0–5" rendered as a
 * data-driven enum. Each level unlocks deeper modules and a more layered
 * atmospheric background.
 *
 * The Home Screen is *not* fixed. It evolves as more emotional signals arrive.
 *
 *  - [Empty] — no birth data, no trackers, no history. Calm + alive.
 *  - [TrackersEnabled] — trackers running, no natal data. Rhythms emerging.
 *  - [NatalEnabled] — birth data added. Deeper patterns visible.
 *  - [TransitsActive] — symbolic climate engine reading the user's current arc.
 *  - [HistoryAccumulated] — enough journal + tracker history to mirror memory.
 *  - [MultiSystem] — MBTI + attachment style + future systems integrated.
 */
enum class NyraHomeStateLevel {
    Empty,
    TrackersEnabled,
    NatalEnabled,
    TransitsActive,
    HistoryAccumulated,
    MultiSystem;

    /** Lower-bound level — used for module gating. */
    fun atLeast(other: NyraHomeStateLevel): Boolean = this.ordinal >= other.ordinal
}

/**
 * Auxiliary signal flags supplied to [HomeStateLevelResolver].
 *
 * Held separately from [NyraUserProfile] so the synth pipeline doesn't have to
 * know about UI state level; resolver composes them locally at render time.
 */
data class NyraHomeStateSignals(
    /** True if at least one tracker (mood, sleep, social energy, …) is enabled. */
    val trackersEnabled: Boolean = false,
    /** True if the user has supplied birth data. */
    val hasNatalData: Boolean = false,
    /** True if the symbolic transits engine has produced a current reading. */
    val hasActiveTransits: Boolean = false,
    /** Days of accumulated tracker + journal history. */
    val historyDays: Int = 0,
    /** True if at least one secondary personality system (MBTI, attachment) is set. */
    val hasSecondarySystem: Boolean = false
) {
    companion object {
        val Empty = NyraHomeStateSignals()
    }
}
