package com.nyra.app.android.core.content

import com.nyra.app.android.core.content.loader.NyraContentBundle
import com.nyra.app.android.core.content.model.ArchetypeDefinition
import com.nyra.app.android.core.content.model.AreaComboRule
import com.nyra.app.android.core.content.model.AstrologyPlacementDefinition
import com.nyra.app.android.core.content.model.LifeAreaDefinition
import com.nyra.app.android.core.content.model.LocalizedText
import com.nyra.app.android.core.content.model.MbtiResultDefinition
import com.nyra.app.android.core.content.model.ReflectionPrompt
import com.nyra.app.android.core.content.model.SingleTraitInterpretation
import com.nyra.app.android.core.content.model.TraitDefinition
import com.nyra.app.android.core.content.validator.ContentValidationErrorCode
import com.nyra.app.android.core.content.validator.NyraContentValidator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Step 11: NyraContentValidator behaviour against small in-memory fakes.
 *
 * Real content is exercised by NyraContentReferenceIntegrityTest; this file
 * isolates each detection path so a regression in the validator itself is caught
 * even when assets are otherwise clean.
 */
class NyraContentValidatorTest {

    private val validator = NyraContentValidator()

    private fun text(value: String = "ok") = LocalizedText(en = value, ru = value)

    private fun emptyBundle(
        traits: List<TraitDefinition> = emptyList(),
        lifeAreas: List<LifeAreaDefinition> = emptyList(),
        areaComboRules: List<AreaComboRule> = emptyList(),
        mbtiResults: List<MbtiResultDefinition> = emptyList(),
        astrologyPlacements: List<AstrologyPlacementDefinition> = emptyList(),
        singleTraitInterpretations: List<SingleTraitInterpretation> = emptyList(),
        reflectionPrompts: List<ReflectionPrompt> = emptyList(),
        archetypes: List<ArchetypeDefinition> = emptyList()
    ) = NyraContentBundle(
        traits = traits,
        lifeAreas = lifeAreas,
        areaComboRules = areaComboRules,
        mbtiResults = mbtiResults,
        astrologyPlacements = astrologyPlacements,
        singleTraitInterpretations = singleTraitInterpretations,
        reflectionPrompts = reflectionPrompts,
        archetypes = archetypes
    )

    private fun trait(id: String) = TraitDefinition(id = id, category = "emotional")
    private fun area(id: String) = LifeAreaDefinition(id = id, category = "core", priority = 80)

    @Test
    fun emptyBundleProducesNoErrors() {
        assertEquals(emptyList<Any>(), validator.validate(emptyBundle()))
    }

    @Test
    fun detectsDuplicateTraitIds() {
        val errors = validator.validate(emptyBundle(traits = listOf(trait("x"), trait("x"))))
        assertTrue(errors.any { it.code == ContentValidationErrorCode.DuplicateId && it.source == "traits.json" })
    }

    @Test
    fun detectsInvalidTraitReferenceInCombo() {
        val combo = AreaComboRule(
            id = "c1",
            area = "inner_world",
            requiredTraits = listOf("ghost_trait"),
            minimumMatch = 1,
            weight = 0.5,
            fallbackPriority = 70,
            title = text(),
            shortSummary = text(),
            summary = text()
        )
        val errors = validator.validate(
            emptyBundle(
                lifeAreas = listOf(area("inner_world")),
                areaComboRules = listOf(combo)
            )
        )
        assertTrue(errors.any {
            it.code == ContentValidationErrorCode.InvalidReference &&
                it.message.contains("ghost_trait")
        })
    }

    @Test
    fun detectsInvalidAreaReferenceInCombo() {
        val combo = AreaComboRule(
            id = "c1",
            area = "nonexistent_area",
            requiredTraits = listOf("t1"),
            minimumMatch = 1,
            weight = 0.5,
            fallbackPriority = 70,
            title = text(),
            shortSummary = text(),
            summary = text()
        )
        val errors = validator.validate(
            emptyBundle(traits = listOf(trait("t1")), areaComboRules = listOf(combo))
        )
        assertTrue(errors.any {
            it.code == ContentValidationErrorCode.InvalidReference &&
                it.message.contains("nonexistent_area")
        })
    }

    @Test
    fun detectsInvalidWeightOnDimension() {
        val badTrait = TraitDefinition(
            id = "t1",
            category = "emotional",
            dimensions = mapOf("depth" to 1.7)
        )
        val errors = validator.validate(emptyBundle(traits = listOf(badTrait)))
        assertTrue(errors.any { it.code == ContentValidationErrorCode.InvalidWeight })
    }

    @Test
    fun detectsInvalidPriorityOnArea() {
        val badArea = LifeAreaDefinition(id = "x", category = "core", priority = 250)
        val errors = validator.validate(emptyBundle(lifeAreas = listOf(badArea)))
        assertTrue(errors.any { it.code == ContentValidationErrorCode.InvalidPriority })
    }

    @Test
    fun detectsMissingLocalizationInCombo() {
        val combo = AreaComboRule(
            id = "c1",
            area = "inner_world",
            requiredTraits = listOf("t1"),
            minimumMatch = 1,
            weight = 0.5,
            fallbackPriority = 70,
            title = LocalizedText(en = "", ru = ""),     // ← blank
            shortSummary = text(),
            summary = text()
        )
        val errors = validator.validate(
            emptyBundle(
                traits = listOf(trait("t1")),
                lifeAreas = listOf(area("inner_world")),
                areaComboRules = listOf(combo)
            )
        )
        assertTrue(errors.any { it.code == ContentValidationErrorCode.MissingLocalization })
    }

    @Test
    fun detectsInvalidMinimumMatchInCombo() {
        val combo = AreaComboRule(
            id = "c1",
            area = "inner_world",
            requiredTraits = listOf("t1", "t2"),
            minimumMatch = 9,                              // ← > required.size
            weight = 0.5,
            fallbackPriority = 70,
            title = text(),
            shortSummary = text(),
            summary = text()
        )
        val errors = validator.validate(
            emptyBundle(
                traits = listOf(trait("t1"), trait("t2")),
                lifeAreas = listOf(area("inner_world")),
                areaComboRules = listOf(combo)
            )
        )
        assertTrue(errors.any { it.code == ContentValidationErrorCode.InvalidRule })
    }

    @Test
    fun detectsInvalidMinimumMatchInArchetype() {
        val arch = ArchetypeDefinition(
            id = "a1",
            priority = 50,
            title = text(),
            shortSummary = text(),
            requiredTraits = listOf("t1"),
            minimumMatch = 5,                              // ← > required.size
            summary = text()
        )
        val errors = validator.validate(emptyBundle(traits = listOf(trait("t1")), archetypes = listOf(arch)))
        assertTrue(errors.any { it.code == ContentValidationErrorCode.InvalidRule })
    }
}
