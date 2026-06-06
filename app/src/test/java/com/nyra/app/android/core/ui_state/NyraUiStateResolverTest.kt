package com.nyra.app.android.core.ui_state

import com.nyra.app.android.core.profile.model.NyraAreaScore
import com.nyra.app.android.core.profile.model.NyraVisualState
import com.nyra.app.android.core.ui_state.asset.NyraUiStateAssetResolver
import com.nyra.app.android.core.ui_state.composition.NyraScreenCompositionResolver
import com.nyra.app.android.core.ui_state.model.NyraTextTone
import com.nyra.app.android.core.ui_state.resolver.NyraUiStateBlender
import com.nyra.app.android.core.ui_state.resolver.NyraUiStateConfigResolverImpl
import com.nyra.app.android.core.ui_state.resolver.NyraUiStateDefinitions
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class NyraUiStateResolverTest {

    private val assetResolver = NyraUiStateAssetResolver()
    private val definitions = NyraUiStateDefinitions(assetResolver)
    private val resolver = NyraUiStateConfigResolverImpl(
        definitions = definitions,
        blender = NyraUiStateBlender(),
        compositionResolver = NyraScreenCompositionResolver()
    )

    @Test
    fun fallsBackToWarmHorizonWhenProfileHasNoVisualStates() {
        val result = resolver.resolve(profile(visualStates = emptyList()))

        assertEquals(NyraVisualState.WARM_HORIZON, result.primaryState)
        assertTrue(result.moduleOrder.isNotEmpty())
    }

    @Test
    fun blendsSecondaryStateIntoPaletteAndMotion() {
        val primary = definitions.get(NyraVisualState.NIGHT_REFLECTION)
        val blended = NyraUiStateBlender().blend(
            primary = primary,
            secondary = definitions.get(NyraVisualState.GOLD_WARMTH)
        )

        assertNotEquals(primary.palette.accent, blended.palette.accent)
        assertTrue(blended.motion.particleDensity > primary.motion.particleDensity)
    }

    @Test
    fun resolvesTextToneFromPrimaryState() {
        val result = resolver.resolve(
            profile(visualStates = listOf(NyraVisualState.CRYSTAL_CLARITY))
        )

        assertEquals(NyraTextTone.ANALYTICAL, result.textTone)
    }

    @Test
    fun prioritizesProfileModulesBeforeDefaults() {
        val result = resolver.resolve(
            profile(
                preferredHomeModules = listOf("symbolic_patterns"),
                areas = listOf(NyraAreaScore("inner_world", 1.0))
            )
        )

        assertEquals("symbolic_patterns", result.moduleOrder.first())
        assertTrue("inner_world" in result.moduleOrder)
    }

    @Test
    fun resolvesAssetsForEachVisualState() {
        NyraVisualState.entries.forEach { state ->
            val assets = assetResolver.resolve(state)

            assertTrue(assets.backgroundAsset.startsWith("bg_"))
            assertTrue(assets.particleAsset.startsWith("particles_"))
            assertTrue(assets.orbAsset.startsWith("orb_"))
        }
    }
}
