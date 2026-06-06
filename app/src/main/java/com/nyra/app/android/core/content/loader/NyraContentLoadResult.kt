package com.nyra.app.android.core.content.loader

import com.nyra.app.android.core.content.validator.ContentValidationError

sealed interface NyraContentLoadResult {
    data class Success(val bundle: NyraContentBundle) : NyraContentLoadResult
    data class Failure(val errors: List<ContentValidationError>) : NyraContentLoadResult
}
