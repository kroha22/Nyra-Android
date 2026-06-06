package com.nyra.app.android.di

import com.nyra.app.android.core.astrology.calculator.ApproximateEphemerisAstrologyPositionCalculator
import com.nyra.app.android.core.astrology.calculator.AstrologyPositionCalculator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AstrologyModule {

    @Binds
    @Singleton
    abstract fun bindAstrologyPositionCalculator(
        impl: ApproximateEphemerisAstrologyPositionCalculator
    ): AstrologyPositionCalculator
}
