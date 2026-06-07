package com.nyra.app.android.core.content

import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Step 4: every trait/area reference in any content file points to an existing entry.
 *
 * Independent of the validator's own checks — the assertions here read as clean
 * diagnostic output without going through ValidationError formatting.
 */
class NyraContentReferenceIntegrityTest {

    private val bundle = ContentTestFixtures.bundle
    private val traitIds = bundle.traits.map { it.id }.toSet()
    private val areaIds = bundle.lifeAreas.map { it.id }.toSet()

    // ─── area_combo_rules.json ─────────────────────────────────────────────────

    @Test
    fun areaComboRulesReferenceExistingTraitsAndAreas() {
        val badTraits = mutableListOf<String>()
        val badAreas = mutableListOf<String>()
        bundle.areaComboRules.forEach { combo ->
            combo.requiredTraits.filterNot { it in traitIds }
                .forEach { badTraits += "${combo.id}.required_traits → $it" }
            if (combo.area !in areaIds) badAreas += "${combo.id}.area → ${combo.area}"
        }
        assertTrue("Unknown traits in area_combo_rules: $badTraits", badTraits.isEmpty())
        assertTrue("Unknown areas in area_combo_rules: $badAreas", badAreas.isEmpty())
    }

    @Test
    fun areaComboRulesAestheticTagsArePresentWhenUsed() {
        // The schema allows empty aesthetic_tags, but if a combo lists them they must be non-blank strings.
        val bad = bundle.areaComboRules
            .flatMap { combo -> combo.aestheticTags.map { combo.id to it } }
            .filter { (_, tag) -> tag.isBlank() }
        assertTrue("Blank aesthetic_tags entries: $bad", bad.isEmpty())
    }

    // ─── single_trait_interpretations.json ─────────────────────────────────────

    @Test
    fun singleTraitInterpretationsReferenceExistingTraitsAndAreas() {
        val badTraits = mutableListOf<String>()
        val badAreas = mutableListOf<String>()
        bundle.singleTraitInterpretations.forEach { entry ->
            if (entry.trait !in traitIds) badTraits += "${entry.id} → ${entry.trait}"
            if (entry.area !in areaIds) badAreas += "${entry.id} → ${entry.area}"
        }
        assertTrue("Unknown traits in single_trait_interpretations: $badTraits", badTraits.isEmpty())
        assertTrue("Unknown areas in single_trait_interpretations: $badAreas", badAreas.isEmpty())
    }

    // ─── reflection_prompts.json ───────────────────────────────────────────────

    @Test
    fun reflectionPromptsReferenceExistingAreas() {
        val bad = mutableListOf<String>()
        bundle.reflectionPrompts.forEach { prompt ->
            prompt.lifeAreas.filterNot { it in areaIds }
                .forEach { bad += "${prompt.id}.life_areas → $it" }
        }
        assertTrue("Unknown areas in reflection_prompts: $bad", bad.isEmpty())
    }

    @Test
    fun reflectionPromptsHaveNonEmptyTagsAndText() {
        val noTags = bundle.reflectionPrompts.filter { it.tags.isEmpty() }.map { it.id }
        val blankPrompt = bundle.reflectionPrompts.filter {
            it.prompt.en.isBlank() || it.prompt.ru.isBlank()
        }.map { it.id }
        assertTrue("Prompts with no tags: $noTags", noTags.isEmpty())
        assertTrue("Prompts with blank text: $blankPrompt", blankPrompt.isEmpty())
    }

    // ─── archetypes.json ───────────────────────────────────────────────────────

    @Test
    fun archetypesReferenceExistingTraits() {
        val bad = mutableListOf<String>()
        bundle.archetypes.forEach { arch ->
            arch.requiredTraits.filterNot { it in traitIds }
                .forEach { bad += "${arch.id}.required_traits → $it" }
        }
        assertTrue("Unknown traits in archetypes: $bad", bad.isEmpty())
    }

    @Test
    fun archetypeAreaBiasReferencesExistingAreas() {
        val bad = mutableListOf<String>()
        bundle.archetypes.forEach { arch ->
            arch.areaBias.keys.filterNot { it in areaIds }
                .forEach { bad += "${arch.id}.area_bias → $it" }
        }
        assertTrue("Unknown areas in archetype area_bias: $bad", bad.isEmpty())
    }

    // ─── astrology_planet_sign.json ────────────────────────────────────────────

    @Test
    fun astrologyPlacementsReferenceExistingTraits() {
        val bad = mutableListOf<String>()
        bundle.astrologyPlacements.forEach { p ->
            (p.coreTraits + p.shadowTraits + p.growthTraits + p.traitWeights.keys)
                .filterNot { it in traitIds }
                .forEach { bad += "${p.id} → $it" }
        }
        assertTrue("Unknown traits in astrology_planet_sign: $bad", bad.isEmpty())
    }

    @Test
    fun astrologyPlacementsReferenceExistingAreas() {
        val bad = mutableListOf<String>()
        bundle.astrologyPlacements.forEach { p ->
            p.areaWeights.keys.filterNot { it in areaIds }
                .forEach { bad += "${p.id}.area_weights → $it" }
        }
        assertTrue("Unknown areas in astrology_planet_sign: $bad", bad.isEmpty())
    }

    // ─── mbti_results.json ─────────────────────────────────────────────────────

    @Test
    fun mbtiReferencesExistingTraits() {
        val bad = mutableListOf<String>()
        bundle.mbtiResults.forEach { mbti ->
            (mbti.coreTraits + mbti.shadowTraits + mbti.growthTraits + mbti.traitWeights.keys)
                .filterNot { it in traitIds }
                .forEach { bad += "${mbti.id} → $it" }
        }
        assertTrue("Unknown traits in mbti_results: $bad", bad.isEmpty())
    }

    @Test
    fun mbtiReferencesExistingAreas() {
        val bad = mutableListOf<String>()
        bundle.mbtiResults.forEach { mbti ->
            mbti.areaWeights.keys.filterNot { it in areaIds }
                .forEach { bad += "${mbti.id}.area_weights → $it" }
        }
        assertTrue("Unknown areas in mbti_results: $bad", bad.isEmpty())
    }

    // ─── life_areas.json primary_traits ────────────────────────────────────────

    @Test
    fun lifeAreaPrimaryTraitsExist() {
        val bad = mutableListOf<String>()
        bundle.lifeAreas.forEach { area ->
            area.primaryTraits.filterNot { it in traitIds }
                .forEach { bad += "${area.id}.primary_traits → $it" }
        }
        assertTrue("Unknown traits in life_areas: $bad", bad.isEmpty())
    }

    // ─── traits.json related/opposite self-references ──────────────────────────

    @Test
    fun traitRelatedAndOppositeReferencesExist() {
        val bad = mutableListOf<String>()
        bundle.traits.forEach { trait ->
            (trait.relatedTraits + trait.oppositeTraits)
                .filterNot { it in traitIds }
                .forEach { bad += "${trait.id} → $it" }
        }
        assertTrue("Unknown trait references inside traits.json: $bad", bad.isEmpty())
    }

    // ─── Backstop: full validator ──────────────────────────────────────────────

    @Test
    fun validatorAcceptsRealBundle() {
        val errors = ContentTestFixtures.validate()
        assertTrue(
            "Validator found errors:\n" + errors.joinToString("\n") {
                "  [${it.code}] ${it.source} ${it.id ?: ""}: ${it.message}"
            },
            errors.isEmpty()
        )
    }
}
