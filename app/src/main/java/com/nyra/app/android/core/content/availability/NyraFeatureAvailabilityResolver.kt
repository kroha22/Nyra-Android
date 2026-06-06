package com.nyra.app.android.core.content.availability

import com.nyra.app.android.core.content.cache.NyraContentCache
import com.nyra.app.android.core.content.validator.ContentValidationError
import javax.inject.Inject

class NyraFeatureAvailabilityResolver @Inject constructor() {

    fun resolve(
        cache: NyraContentCache?,
        errors: List<ContentValidationError>
    ): NyraFeatureState {
        val contentReady = cache != null && errors.isEmpty()
        return NyraFeatureState(
            contentReady = contentReady,
            mbtiEnabled = contentReady && cache?.mbtiByType?.isNotEmpty() == true,
            astrologyEnabled = contentReady && cache?.astrologyByPlacementId?.isNotEmpty() == true,
            reflectionEnabled = contentReady && cache?.promptsByTag?.isNotEmpty() == true,
            archetypesEnabled = contentReady && cache?.archetypesById?.isNotEmpty() == true,
            validationErrors = errors
        )
    }
}
