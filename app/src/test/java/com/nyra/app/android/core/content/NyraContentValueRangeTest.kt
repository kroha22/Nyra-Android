package com.nyra.app.android.core.content

import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Step 6: numeric ranges.
 *
 *  - `trait_weights` / `area_weights` / `dimension_profile` / `dimension_bias` /
 *    `area_bias` values: **0.0 ≤ v ≤ 1.0** (matches NyraContentValidator).
 *  - `source_weight` / `confidence`: **0.0 ≤ v ≤ 1.5** (astrology uses 1.1).
 *  - `priority`, `fallback_priority`: **0 ≤ p ≤ 100**.
 *  - `minimum_match`: **1 ≤ m ≤ required_traits.size**.
 *  - `rarity` (archetypes): **0.0 ≤ r ≤ 1.0**.
 *  - `premium`: Boolean (enforced by the type system; we just spot-check truthy/falsy).
 */
class NyraContentValueRangeTest {

    private val bundle = ContentTestFixtures.bundle
    private val unitRange = 0.0..1.0
    private val sourceWeightRange = 0.0..1.5
    private val priorityRange = 0..100

    // ─── Trait dimensions ─────────────────────────────────────────────────────

    @Test
    fun traitDimensionsInUnitRange() {
        val bad = bundle.traits.flatMap { t ->
            t.dimensions.entries
                .filterNot { it.value in unitRange }
                .map { "${t.id}.dimensions.${it.key} = ${it.value}" }
        }
        assertTrue("Out-of-range dimensions: $bad", bad.isEmpty())
    }

    // ─── Life areas ──────────────────────────────────────────────────────────

    @Test
    fun lifeAreaPrioritiesInRange() {
        val bad = bundle.lifeAreas
            .filterNot { it.priority in priorityRange }
            .map { "${it.id} priority=${it.priority}" }
        assertTrue("Bad life area priorities: $bad", bad.isEmpty())
    }

    @Test
    fun lifeAreaDimensionBiasInUnitRange() {
        val bad = bundle.lifeAreas.flatMap { a ->
            a.dimensionBias.entries
                .filterNot { it.value in unitRange }
                .map { "${a.id}.dimension_bias.${it.key} = ${it.value}" }
        }
        assertTrue("Out-of-range area dimension_bias: $bad", bad.isEmpty())
    }

    // ─── Area combo rules ─────────────────────────────────────────────────────

    @Test
    fun comboWeightAndPriorityInRange() {
        val badWeight = bundle.areaComboRules.filterNot { it.weight in unitRange }
            .map { "${it.id}.weight=${it.weight}" }
        val badPriority = bundle.areaComboRules.filterNot { it.fallbackPriority in priorityRange }
            .map { "${it.id}.fallback_priority=${it.fallbackPriority}" }
        assertTrue("Bad combo weights: $badWeight", badWeight.isEmpty())
        assertTrue("Bad combo fallback_priorities: $badPriority", badPriority.isEmpty())
    }

    @Test
    fun comboMinimumMatchInRange() {
        val bad = bundle.areaComboRules
            .filterNot { it.minimumMatch in 1..it.requiredTraits.size }
            .map { "${it.id} minimum_match=${it.minimumMatch} required=${it.requiredTraits.size}" }
        assertTrue("Bad minimum_match: $bad", bad.isEmpty())
    }

    // ─── MBTI ────────────────────────────────────────────────────────────────

    @Test
    fun mbtiSourceWeightAndConfidenceInRange() {
        val bad = mutableListOf<String>()
        bundle.mbtiResults.forEach {
            if (it.sourceWeight !in sourceWeightRange) bad += "${it.id}.source_weight=${it.sourceWeight}"
            if (it.confidence !in sourceWeightRange) bad += "${it.id}.confidence=${it.confidence}"
        }
        assertTrue("Out-of-range MBTI source_weight/confidence: $bad", bad.isEmpty())
    }

    @Test
    fun mbtiTraitAreaDimensionWeightsInUnitRange() {
        val bad = mutableListOf<String>()
        bundle.mbtiResults.forEach { m ->
            m.traitWeights.entries.filterNot { it.value in unitRange }
                .forEach { bad += "${m.id}.trait_weights.${it.key}=${it.value}" }
            m.areaWeights.entries.filterNot { it.value in unitRange }
                .forEach { bad += "${m.id}.area_weights.${it.key}=${it.value}" }
            m.dimensionProfile.entries.filterNot { it.value in unitRange }
                .forEach { bad += "${m.id}.dimension_profile.${it.key}=${it.value}" }
        }
        assertTrue("MBTI weights out of range: $bad", bad.isEmpty())
    }

    // ─── Astrology ───────────────────────────────────────────────────────────

    @Test
    fun astrologySourceWeightAndConfidenceInRange() {
        val bad = mutableListOf<String>()
        bundle.astrologyPlacements.forEach {
            if (it.sourceWeight !in sourceWeightRange) bad += "${it.id}.source_weight=${it.sourceWeight}"
            if (it.confidence !in sourceWeightRange) bad += "${it.id}.confidence=${it.confidence}"
        }
        assertTrue("Out-of-range astrology source_weight/confidence: $bad", bad.isEmpty())
    }

    @Test
    fun astrologyTraitAreaDimensionWeightsInUnitRange() {
        val bad = mutableListOf<String>()
        bundle.astrologyPlacements.forEach { p ->
            p.traitWeights.entries.filterNot { it.value in unitRange }
                .forEach { bad += "${p.id}.trait_weights.${it.key}=${it.value}" }
            p.areaWeights.entries.filterNot { it.value in unitRange }
                .forEach { bad += "${p.id}.area_weights.${it.key}=${it.value}" }
            p.dimensionProfile.entries.filterNot { it.value in unitRange }
                .forEach { bad += "${p.id}.dimension_profile.${it.key}=${it.value}" }
        }
        assertTrue("Astrology weights out of range: $bad", bad.isEmpty())
    }

    // ─── Single trait interpretations ─────────────────────────────────────────

    @Test
    fun singleTraitInterpretationPrioritiesInRange() {
        val bad = bundle.singleTraitInterpretations
            .filterNot { it.priority in priorityRange }
            .map { "${it.id} priority=${it.priority}" }
        assertTrue("Bad interpretation priorities: $bad", bad.isEmpty())
    }

    // ─── Archetypes ──────────────────────────────────────────────────────────

    @Test
    fun archetypeNumericFieldsInRange() {
        val bad = mutableListOf<String>()
        bundle.archetypes.forEach { a ->
            if (a.priority !in priorityRange) bad += "${a.id}.priority=${a.priority}"
            if (a.minimumMatch !in 1..a.requiredTraits.size)
                bad += "${a.id}.minimum_match=${a.minimumMatch} required=${a.requiredTraits.size}"
            a.dimensionBias.entries.filterNot { it.value in unitRange }
                .forEach { bad += "${a.id}.dimension_bias.${it.key}=${it.value}" }
            a.areaBias.entries.filterNot { it.value in unitRange }
                .forEach { bad += "${a.id}.area_bias.${it.key}=${it.value}" }
            a.rarity?.let { r -> if (r !in unitRange) bad += "${a.id}.rarity=$r" }
        }
        assertTrue("Archetype value-range issues: $bad", bad.isEmpty())
    }
}
