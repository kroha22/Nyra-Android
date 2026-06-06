package com.nyra.app.android.core.content.validator

data class ContentValidationError(
    val code: ContentValidationErrorCode,
    val source: String,
    val message: String,
    val id: String? = null
)

enum class ContentValidationErrorCode {
    MissingFile,
    ParseError,
    DuplicateId,
    MissingLocalization,
    InvalidWeight,
    InvalidPriority,
    InvalidReference,
    InvalidRule
}
