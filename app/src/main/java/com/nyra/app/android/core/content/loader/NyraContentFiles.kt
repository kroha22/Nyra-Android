package com.nyra.app.android.core.content.loader

object NyraContentFiles {
    const val ROOT = "canonical"

    const val TRAITS = "$ROOT/traits.json"
    const val LIFE_AREAS = "$ROOT/life_areas.json"
    const val AREA_COMBO_RULES = "$ROOT/area_combo_rules.json"
    const val MBTI_RESULTS = "$ROOT/mbti_results.json"
    const val ASTROLOGY_PLANET_SIGN = "$ROOT/astrology_planet_sign.json"
    const val SINGLE_TRAIT_INTERPRETATIONS = "$ROOT/single_trait_interpretations.json"
    const val REFLECTION_PROMPTS = "$ROOT/reflection_prompts.json"
    const val ARCHETYPES = "$ROOT/archetypes.json"

    val required: List<String> = listOf(
        TRAITS,
        LIFE_AREAS,
        AREA_COMBO_RULES,
        MBTI_RESULTS,
        ASTROLOGY_PLANET_SIGN,
        SINGLE_TRAIT_INTERPRETATIONS,
        REFLECTION_PROMPTS,
        ARCHETYPES
    )
}
