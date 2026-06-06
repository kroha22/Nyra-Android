package com.nyra.app.android.core.profile.mapper

import com.nyra.app.android.core.astrology.model.Planet
import com.nyra.app.android.core.astrology.model.ZodiacSign

fun Planet.toContentId(): String = name.lowercase()

fun ZodiacSign.toContentId(): String = name.lowercase()
