package com.nyra.app.android.core.ui_state.asset

import com.nyra.app.android.core.profile.model.NyraVisualState
import javax.inject.Inject

data class NyraUiStateAssets(
    val backgroundAsset: String,
    val gradientAsset: String,
    val particleAsset: String,
    val orbAsset: String,
    val motionAsset: String
)

class NyraUiStateAssetResolver @Inject constructor() {

    fun resolve(state: NyraVisualState): NyraUiStateAssets =
        assets[state] ?: assets.getValue(NyraVisualState.WARM_HORIZON)

    private companion object {
        val assets = mapOf(
            NyraVisualState.WARM_HORIZON to NyraUiStateAssets(
                backgroundAsset = "bg_warm_horizon",
                gradientAsset = "gradient_warm_horizon",
                particleAsset = "particles_soft_dust",
                orbAsset = "orb_warm_soft",
                motionAsset = "motion_slow_drift"
            ),
            NyraVisualState.NIGHT_REFLECTION to NyraUiStateAssets(
                backgroundAsset = "bg_night_reflection",
                gradientAsset = "gradient_night_reflection",
                particleAsset = "particles_mist",
                orbAsset = "orb_moon_slow",
                motionAsset = "motion_almost_still"
            ),
            NyraVisualState.DEEP_WATER to NyraUiStateAssets(
                backgroundAsset = "bg_deep_water",
                gradientAsset = "gradient_deep_water",
                particleAsset = "particles_water_drift",
                orbAsset = "orb_water_glow",
                motionAsset = "motion_fluid_drift"
            ),
            NyraVisualState.GOLD_WARMTH to NyraUiStateAssets(
                backgroundAsset = "bg_gold_warmth",
                gradientAsset = "gradient_gold_warmth",
                particleAsset = "particles_gold_spark",
                orbAsset = "orb_sun_soft",
                motionAsset = "motion_warm_breathe"
            ),
            NyraVisualState.SOFT_DAWN to NyraUiStateAssets(
                backgroundAsset = "bg_soft_dawn",
                gradientAsset = "gradient_soft_dawn",
                particleAsset = "particles_soft_light",
                orbAsset = "orb_pearl",
                motionAsset = "motion_soft_rise"
            ),
            NyraVisualState.CRYSTAL_CLARITY to NyraUiStateAssets(
                backgroundAsset = "bg_crystal_clarity",
                gradientAsset = "gradient_crystal_clarity",
                particleAsset = "particles_minimal",
                orbAsset = "orb_clear_glass",
                motionAsset = "motion_precise_slow"
            ),
            NyraVisualState.HORIZON_MOTION to NyraUiStateAssets(
                backgroundAsset = "bg_horizon_motion",
                gradientAsset = "gradient_horizon_motion",
                particleAsset = "particles_wind",
                orbAsset = "orb_compass",
                motionAsset = "motion_open_drift"
            ),
            NyraVisualState.VELVET_INTIMACY to NyraUiStateAssets(
                backgroundAsset = "bg_velvet_intimacy",
                gradientAsset = "gradient_velvet_intimacy",
                particleAsset = "particles_soft_embers",
                orbAsset = "orb_heart_glow",
                motionAsset = "motion_close_breathe"
            )
        )
    }
}
