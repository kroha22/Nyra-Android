package com.nyra.app.android.core.ui_state.resolver

import com.nyra.app.android.core.model.TimeOfDay
import com.nyra.app.android.core.profile.model.NyraUserProfile
import com.nyra.app.android.core.profile.model.NyraVisualState
import com.nyra.app.android.core.rules.TimeOfDayResolver
import com.nyra.app.android.core.ui_state.composition.NyraScreenCompositionResolver
import com.nyra.app.android.core.ui_state.model.NyraUiStateConfig
import javax.inject.Inject

class NyraUiStateConfigResolverImpl @Inject constructor(
    private val definitions: NyraUiStateDefinitions,
    private val blender: NyraUiStateBlender,
    private val compositionResolver: NyraScreenCompositionResolver,
    private val timeOfDayResolver: TimeOfDayResolver,
    private val timeModulator: NyraAtmosphereTimeModulator
) : NyraUiStateConfigResolver {

    override fun resolve(
        profile: NyraUserProfile,
        timeOfDay: TimeOfDay?
    ): NyraUiStateConfig {
        val primaryState = profile.visualStates.firstOrNull() ?: NyraVisualState.WARM_HORIZON
        val secondaryState = profile.visualStates.drop(1).firstOrNull()
        val primary = definitions.get(primaryState)
        val secondary = secondaryState?.let { definitions.get(it) }
        val blended = blender.blend(primary, secondary)
        val moduleOrder = compositionResolver.resolveModuleOrder(
            profile = profile,
            primaryDefaultModules = primary.defaultModules,
            secondaryDefaultModules = secondary?.defaultModules.orEmpty()
        )

        val base = NyraUiStateConfig(
            primaryState = primaryState,
            secondaryState = secondaryState,
            palette = blended.palette,
            background = blended.background,
            motion = blended.motion,
            cardStyle = blended.cardStyle,
            orb = blended.orb,
            textTone = primary.textTone,
            moduleOrder = moduleOrder
        )

        // Apply subtle time-of-day modulation last — preserves state identity,
        // only nudges atmospheric overlay parameters (blur, glow, motion, particles).
        val effectiveTime = timeOfDay ?: timeOfDayResolver.resolve()
        return timeModulator.modulate(base, effectiveTime)
    }
}
