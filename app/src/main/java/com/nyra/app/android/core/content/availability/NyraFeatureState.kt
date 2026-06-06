package com.nyra.app.android.core.content.availability

import com.nyra.app.android.core.content.validator.ContentValidationError

data class NyraFeatureState(
    val contentReady: Boolean,
    val mbtiEnabled: Boolean,
    val astrologyEnabled: Boolean,
    val reflectionEnabled: Boolean,
    val archetypesEnabled: Boolean,
    val validationErrors: List<ContentValidationError>
) {
    val isContentReady: Boolean
        get() = contentReady
}
