package com.nyra.app.android.core.profile.matcher

import com.nyra.app.android.core.content.repository.NyraContentRepository
import com.nyra.app.android.core.profile.model.NyraActiveCombo
import com.nyra.app.android.core.profile.model.NyraAreaScore
import com.nyra.app.android.core.profile.model.NyraTraitScore
import javax.inject.Inject

class ComboMatcher @Inject constructor(
    private val repository: NyraContentRepository
) {

    fun match(
        topTraits: List<NyraTraitScore>,
        areas: List<NyraAreaScore>
    ): List<NyraActiveCombo> {
        val traitIds = topTraits.map { it.traitId }.toSet()
        return areas
            .take(MAX_AREAS_TO_SCAN)
            .flatMap { area -> repository.getCombosForArea(area.areaId) }
            .distinctBy { it.id }
            .mapNotNull { combo ->
                val required = combo.requiredTraits.toSet()
                if (required.isEmpty()) return@mapNotNull null
                val matched = required.count { it in traitIds }
                if (matched < combo.minimumMatch) return@mapNotNull null
                val matchRatio = matched.toDouble() / required.size
                NyraActiveCombo(
                    comboId = combo.id,
                    score = (matchRatio * combo.weight).coerceIn(0.0, 1.0)
                )
            }
            .sortedWith(compareByDescending<NyraActiveCombo> { it.score }.thenBy { it.comboId })
    }

    private companion object {
        const val MAX_AREAS_TO_SCAN = 12
    }
}
