package com.nyra.app.android.core.content.availability

import com.nyra.app.android.core.content.validator.ContentValidationError

sealed interface NyraContentAvailability {
    data object NotLoaded : NyraContentAvailability
    data object Available : NyraContentAvailability
    data class Unavailable(
        val reason: String,
        val errors: List<ContentValidationError> = emptyList()
    ) : NyraContentAvailability
}
