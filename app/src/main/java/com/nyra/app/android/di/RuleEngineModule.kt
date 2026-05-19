package com.nyra.app.android.di

import com.nyra.app.android.core.rules.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RuleEngineModule {

    @Provides
    @Singleton
    fun provideTimeOfDayResolver(): TimeOfDayResolver = TimeOfDayResolver()

    @Provides
    @Singleton
    fun providePresenceStateResolver(): PresenceStateResolver = PresenceStateResolver()

    @Provides
    @Singleton
    fun provideHomeCardResolver(): HomeCardResolver = HomeCardResolver()

    @Provides
    @Singleton
    fun provideGentleActionResolver(): GentleActionResolver = GentleActionResolver()

    @Provides
    @Singleton
    fun provideRuleEngine(
        timeOfDayResolver: TimeOfDayResolver,
        presenceStateResolver: PresenceStateResolver,
        homeCardResolver: HomeCardResolver,
        gentleActionResolver: GentleActionResolver
    ): RuleEngine = RuleEngine(
        timeOfDayResolver,
        presenceStateResolver,
        homeCardResolver,
        gentleActionResolver
    )
}