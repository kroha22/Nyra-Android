package com.nyra.app.android.core.profile.synthesis

import com.nyra.app.android.core.astrology.model.AstrologyCalculationResult
import com.nyra.app.android.core.content.model.ArchetypeDefinition
import com.nyra.app.android.core.content.model.AreaComboRule
import com.nyra.app.android.core.content.model.AstrologyPlacementDefinition
import com.nyra.app.android.core.content.model.LocalizedText
import com.nyra.app.android.core.content.model.MbtiResultDefinition
import com.nyra.app.android.core.content.repository.NyraContentRepository
import com.nyra.app.android.core.profile.mapper.toContentId
import com.nyra.app.android.core.profile.matcher.ArchetypeMatcher
import com.nyra.app.android.core.profile.matcher.ComboMatcher
import com.nyra.app.android.core.profile.model.NyraUserProfile
import com.nyra.app.android.core.profile.resolver.HomeModuleResolver
import com.nyra.app.android.core.profile.resolver.VisualStateResolver
import javax.inject.Inject

class NyraProfileSynthesizerImpl @Inject constructor(
    private val repository: NyraContentRepository,
    private val traitAggregator: TraitScoreAggregator,
    private val dimensionAggregator: DimensionAggregator,
    private val areaScoreCalculator: AreaScoreCalculator,
    private val comboMatcher: ComboMatcher,
    private val archetypeMatcher: ArchetypeMatcher,
    private val visualStateResolver: VisualStateResolver,
    private val homeModuleResolver: HomeModuleResolver
) : NyraProfileSynthesizer {

    override fun synthesizeFromAstrology(
        calculationResult: AstrologyCalculationResult
    ): NyraUserProfile = synthesize(calculationResult = calculationResult, mbtiType = null)

    override fun synthesize(
        calculationResult: AstrologyCalculationResult,
        mbtiType: String?
    ): NyraUserProfile {
        val warnings = calculationResult.warnings.toMutableList()

        val placementDefinitions = calculationResult.placements.mapNotNull { placement ->
            val planet = placement.planet.toContentId()
            val sign = placement.sign.toContentId()
            repository.getAstrologyPlacement(planet, sign).also { definition ->
                if (definition == null) {
                    warnings += "Missing astrology content record for ${planet}_${sign}."
                }
            }
        }

        val mbtiResult = mbtiType
            ?.takeIf { it.isNotBlank() }
            ?.let { type ->
                repository.getMbtiResult(type).also { result ->
                    if (result == null) warnings += "Missing MBTI content record for $type."
                }
            }

        val topTraits = traitAggregator.aggregateTopTraits(placementDefinitions, mbtiResult)
        val shadowTraits = traitAggregator.aggregateShadowTraits(placementDefinitions, mbtiResult)
        val growthTraits = traitAggregator.aggregateGrowthTraits(placementDefinitions, mbtiResult)
        val dimensions = dimensionAggregator.aggregate(placementDefinitions, topTraits, mbtiResult)
        val areas = areaScoreCalculator.calculate(
            placements = placementDefinitions,
            topTraits = topTraits,
            dimensions = dimensions,
            mbtiResult = mbtiResult
        )
        val activeCombos = comboMatcher.match(topTraits, areas)
        val comboDefinitions = activeCombos.mapNotNull { activeCombo ->
            areas
                .flatMap { area -> repository.getCombosForArea(area.areaId) }
                .firstOrNull { it.id == activeCombo.comboId }
        }
        val activeArchetypes = archetypeMatcher.match(topTraits, dimensions, areas)
        val archetypeDefinitions = activeArchetypes.mapNotNull { repository.getArchetype(it.archetypeId) }

        val aestheticTags = collectAestheticTags(
            placements = placementDefinitions,
            mbtiResult = mbtiResult,
            comboDefinitions = comboDefinitions,
            archetypeDefinitions = archetypeDefinitions,
            topTraitIds = topTraits.map { it.traitId }
        )
        val reflectionTags = collectReflectionTags(
            placements = placementDefinitions,
            mbtiResult = mbtiResult,
            comboDefinitions = comboDefinitions,
            archetypeDefinitions = archetypeDefinitions,
            topTraitIds = topTraits.map { it.traitId }
        )
        val visualStates = visualStateResolver.resolve(
            placements = placementDefinitions,
            activeArchetypes = archetypeDefinitions,
            aestheticTags = aestheticTags,
            dimensions = dimensions
        )
        val preferredHomeModules = homeModuleResolver.resolve(
            placements = placementDefinitions,
            areas = areas,
            activeArchetypes = archetypeDefinitions
        )

        return NyraUserProfile(
            placements = calculationResult.placements,
            topTraits = topTraits,
            shadowTraits = shadowTraits,
            growthTraits = growthTraits,
            dimensions = dimensions,
            areas = areas,
            activeCombos = activeCombos,
            activeArchetypes = activeArchetypes,
            visualStates = visualStates,
            aestheticTags = aestheticTags,
            reflectionTags = reflectionTags,
            preferredHomeModules = preferredHomeModules,
            summaries = collectTextBlocks(
                placements = placementDefinitions,
                mbtiResult = mbtiResult,
                comboDefinitions = comboDefinitions,
                archetypeDefinitions = archetypeDefinitions,
                selector = { it.summary }
            ),
            shadows = collectTextBlocks(
                placements = placementDefinitions,
                mbtiResult = mbtiResult,
                comboDefinitions = comboDefinitions,
                archetypeDefinitions = archetypeDefinitions,
                selector = { it.shadow }
            ),
            growths = collectTextBlocks(
                placements = placementDefinitions,
                mbtiResult = mbtiResult,
                comboDefinitions = comboDefinitions,
                archetypeDefinitions = archetypeDefinitions,
                selector = { it.growth }
            ),
            warnings = warnings.stableDistinct()
        )
    }

    private fun collectAestheticTags(
        placements: List<AstrologyPlacementDefinition>,
        mbtiResult: MbtiResultDefinition?,
        comboDefinitions: List<AreaComboRule>,
        archetypeDefinitions: List<ArchetypeDefinition>,
        topTraitIds: List<String>
    ): List<String> =
        (
            placements.flatMap { it.aestheticTags } +
                mbtiResult?.aestheticTags.orEmpty() +
                comboDefinitions.flatMap { it.aestheticTags } +
                archetypeDefinitions.flatMap { it.aestheticTags } +
                topTraitIds.take(TAG_TRAIT_LIMIT).flatMap { traitId ->
                    repository.getTrait(traitId)?.aestheticTags.orEmpty()
                }
            ).stableDistinct()

    private fun collectReflectionTags(
        placements: List<AstrologyPlacementDefinition>,
        mbtiResult: MbtiResultDefinition?,
        comboDefinitions: List<AreaComboRule>,
        archetypeDefinitions: List<ArchetypeDefinition>,
        topTraitIds: List<String>
    ): List<String> =
        (
            placements.flatMap { it.reflectionTags } +
                mbtiResult?.reflectionTags.orEmpty() +
                comboDefinitions.flatMap { it.reflectionTags } +
                archetypeDefinitions.flatMap { it.reflectionTags } +
                topTraitIds.take(TAG_TRAIT_LIMIT).flatMap { traitId ->
                    repository.getTrait(traitId)?.reflectionTags.orEmpty()
                }
            ).stableDistinct()

    private fun collectTextBlocks(
        placements: List<AstrologyPlacementDefinition>,
        mbtiResult: MbtiResultDefinition?,
        comboDefinitions: List<AreaComboRule>,
        archetypeDefinitions: List<ArchetypeDefinition>,
        selector: (TextSource) -> LocalizedText?
    ): List<String> {
        val sources: List<TextSource> =
            placements.map { TextSource.Placement(it) } +
                listOfNotNull(mbtiResult?.let { TextSource.Mbti(it) }) +
                comboDefinitions.map { TextSource.Combo(it) } +
                archetypeDefinitions.map { TextSource.Archetype(it) }

        return sources
            .mapNotNull { source -> selector(source)?.en?.takeIf { it.isNotBlank() } }
            .stableDistinct()
    }

    private sealed interface TextSource {
        val summary: LocalizedText?
        val shadow: LocalizedText?
        val growth: LocalizedText?

        data class Placement(
            private val value: AstrologyPlacementDefinition
        ) : TextSource {
            override val summary: LocalizedText = value.summary
            override val shadow: LocalizedText? = value.shadow
            override val growth: LocalizedText? = value.growth
        }

        data class Mbti(
            private val value: MbtiResultDefinition
        ) : TextSource {
            override val summary: LocalizedText = value.summary
            override val shadow: LocalizedText? = value.shadow
            override val growth: LocalizedText? = value.growth
        }

        data class Combo(
            private val value: AreaComboRule
        ) : TextSource {
            override val summary: LocalizedText = value.summary
            override val shadow: LocalizedText? = value.shadow
            override val growth: LocalizedText? = value.growth
        }

        data class Archetype(
            private val value: ArchetypeDefinition
        ) : TextSource {
            override val summary: LocalizedText = value.summary
            override val shadow: LocalizedText? = value.shadow
            override val growth: LocalizedText? = value.growth
        }
    }

    private companion object {
        const val TAG_TRAIT_LIMIT = 16
    }
}
