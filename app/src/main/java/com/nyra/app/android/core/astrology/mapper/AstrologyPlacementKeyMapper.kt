package com.nyra.app.android.core.astrology.mapper

import com.nyra.app.android.core.astrology.model.PlanetPlacement
import javax.inject.Inject

class AstrologyPlacementKeyMapper @Inject constructor() {
    fun toPlacementKey(placement: PlanetPlacement): String =
        "${placement.planet.name.lowercase()}_${placement.sign.name.lowercase()}"
}
