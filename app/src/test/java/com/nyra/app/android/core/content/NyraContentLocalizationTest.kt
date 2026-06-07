package com.nyra.app.android.core.content

import com.nyra.app.android.core.content.model.LocalizedText
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Step 5: every LocalizedText carries non-blank `en` and `ru`.
 *
 * Catches dropped translations early so prompts / cards never render `""` at runtime.
 */
class NyraContentLocalizationTest {

    private val bundle = ContentTestFixtures.bundle

    @Test
    fun areaComboRuleTextsAreLocalized() {
        val bad = mutableListOf<String>()
        bundle.areaComboRules.forEach { c ->
            check(c.id, "title", c.title, bad)
            check(c.id, "short_summary", c.shortSummary, bad)
            check(c.id, "summary", c.summary, bad)
            c.shadow?.let { check(c.id, "shadow", it, bad) }
            c.growth?.let { check(c.id, "growth", it, bad) }
        }
        assertNone("area_combo_rules.json", bad)
    }

    @Test
    fun mbtiResultTextsAreLocalized() {
        val bad = mutableListOf<String>()
        bundle.mbtiResults.forEach { m ->
            check(m.id, "title", m.title, bad)
            check(m.id, "short_summary", m.shortSummary, bad)
            check(m.id, "summary", m.summary, bad)
            m.shadow?.let { check(m.id, "shadow", it, bad) }
            m.growth?.let { check(m.id, "growth", it, bad) }
        }
        assertNone("mbti_results.json", bad)
    }

    @Test
    fun astrologyPlacementTextsAreLocalized() {
        val bad = mutableListOf<String>()
        bundle.astrologyPlacements.forEach { p ->
            check(p.id, "title", p.title, bad)
            check(p.id, "short_summary", p.shortSummary, bad)
            check(p.id, "summary", p.summary, bad)
            p.shadow?.let { check(p.id, "shadow", it, bad) }
            p.growth?.let { check(p.id, "growth", it, bad) }
        }
        assertNone("astrology_planet_sign.json", bad)
    }

    @Test
    fun singleTraitInterpretationTextsAreLocalized() {
        val bad = mutableListOf<String>()
        bundle.singleTraitInterpretations.forEach { s ->
            check(s.id, "title", s.title, bad)
            check(s.id, "short_summary", s.shortSummary, bad)
            check(s.id, "summary", s.summary, bad)
        }
        assertNone("single_trait_interpretations.json", bad)
    }

    @Test
    fun reflectionPromptTextsAreLocalized() {
        val bad = mutableListOf<String>()
        bundle.reflectionPrompts.forEach { p ->
            check(p.id, "prompt", p.prompt, bad)
            p.followupPrompt?.let { check(p.id, "followup_prompt", it, bad) }
        }
        assertNone("reflection_prompts.json", bad)
    }

    @Test
    fun archetypeTextsAreLocalized() {
        val bad = mutableListOf<String>()
        bundle.archetypes.forEach { a ->
            check(a.id, "title", a.title, bad)
            check(a.id, "short_summary", a.shortSummary, bad)
            check(a.id, "summary", a.summary, bad)
            a.shadow?.let { check(a.id, "shadow", it, bad) }
            a.growth?.let { check(a.id, "growth", it, bad) }
        }
        assertNone("archetypes.json", bad)
    }

    private fun check(ownerId: String, field: String, text: LocalizedText, sink: MutableList<String>) {
        if (text.en.isBlank()) sink += "$ownerId.$field.en is blank"
        if (text.ru.isBlank()) sink += "$ownerId.$field.ru is blank"
    }

    private fun assertNone(source: String, missing: List<String>) {
        assertTrue("$source missing localization:\n  ${missing.joinToString("\n  ")}", missing.isEmpty())
    }
}
