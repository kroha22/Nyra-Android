package com.nyra.app.android.core.content.repository

import com.nyra.app.android.core.content.availability.NyraContentAvailability
import com.nyra.app.android.core.content.availability.NyraFeatureAvailabilityResolver
import com.nyra.app.android.core.content.availability.NyraFeatureState
import com.nyra.app.android.core.content.cache.NyraContentCache
import com.nyra.app.android.core.content.loader.NyraContentLoadResult
import com.nyra.app.android.core.content.loader.NyraContentLoader
import com.nyra.app.android.core.content.model.ArchetypeDefinition
import com.nyra.app.android.core.content.model.AreaComboRule
import com.nyra.app.android.core.content.model.AstrologyPlacementDefinition
import com.nyra.app.android.core.content.model.LifeAreaDefinition
import com.nyra.app.android.core.content.model.MbtiResultDefinition
import com.nyra.app.android.core.content.model.ReflectionPrompt
import com.nyra.app.android.core.content.model.SingleTraitInterpretation
import com.nyra.app.android.core.content.model.TraitDefinition
import com.nyra.app.android.core.content.validator.ContentValidationError
import com.nyra.app.android.core.content.validator.NyraContentValidator
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NyraContentRepositoryImpl @Inject constructor(
    private val loader: NyraContentLoader,
    private val validator: NyraContentValidator,
    private val availabilityResolver: NyraFeatureAvailabilityResolver
) : NyraContentRepository {

    private val lock = Any()

    @Volatile
    private var cache: NyraContentCache? = null

    @Volatile
    private var availability: NyraContentAvailability = NyraContentAvailability.NotLoaded

    @Volatile
    private var featureState: NyraFeatureState = availabilityResolver.resolve(
        cache = null,
        errors = emptyList()
    )

    override suspend fun loadContent(): NyraContentAvailability =
        synchronized(lock) {
            when (val result = loader.load()) {
                is NyraContentLoadResult.Failure -> {
                    applyUnavailable(
                        reason = "Nyra content assets could not be loaded",
                        errors = result.errors
                    )
                }
                is NyraContentLoadResult.Success -> {
                    val validationErrors = validator.validate(result.bundle)
                    if (validationErrors.isEmpty()) {
                        cache = NyraContentCache.from(result.bundle)
                        availability = NyraContentAvailability.Available
                        featureState = availabilityResolver.resolve(cache, emptyList())
                    } else {
                        applyUnavailable(
                            reason = "Nyra content assets failed validation",
                            errors = validationErrors
                        )
                    }
                }
            }
            availability
        }

    override fun getContentState(): NyraFeatureState = featureState

    override fun getTrait(id: String): TraitDefinition? = cache?.traitsById?.get(id)

    override fun getArea(id: String): LifeAreaDefinition? = cache?.areasById?.get(id)

    override fun getMbtiResult(type: String): MbtiResultDefinition? =
        cache?.mbtiByType?.get(type.uppercase())

    override fun getAstrologyPlacement(
        planet: String,
        sign: String
    ): AstrologyPlacementDefinition? =
        cache?.astrologyByPlanetSign?.get(NyraContentCache.placementKey(planet, sign))

    override fun getCombosForArea(areaId: String): List<AreaComboRule> =
        cache?.combosByArea?.get(areaId).orEmpty()

    override fun getSingleTraitInterpretations(
        areaId: String,
        traitIds: Collection<String>
    ): List<SingleTraitInterpretation> {
        val traitSet = traitIds.toSet()
        return cache?.singleTraitByArea?.get(areaId)
            .orEmpty()
            .filter { it.trait in traitSet }
            .sortedByDescending { it.priority }
    }

    override fun getPromptsByTags(tags: Collection<String>): List<ReflectionPrompt> {
        val tagSet = tags.toSet()
        return tagSet
            .flatMap { tag -> cache?.promptsByTag?.get(tag).orEmpty() }
            .distinctBy { it.id }
            .sortedWith(
                compareByDescending<ReflectionPrompt> { prompt ->
                    (prompt.tags + prompt.reflectionTags).count { it in tagSet }
                }.thenBy { it.id }
            )
    }

    override fun getArchetype(id: String): ArchetypeDefinition? =
        cache?.archetypesById?.get(id)

    override fun getArchetypes(): List<ArchetypeDefinition> =
        cache?.archetypesById
            .orEmpty()
            .values
            .sortedWith(compareByDescending<ArchetypeDefinition> { it.priority }.thenBy { it.id })

    private fun applyUnavailable(
        reason: String,
        errors: List<ContentValidationError>
    ) {
        cache = null
        availability = NyraContentAvailability.Unavailable(reason = reason, errors = errors)
        featureState = availabilityResolver.resolve(cache = null, errors = errors)
    }
}
