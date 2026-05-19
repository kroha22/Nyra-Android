package com.nyra.app.android.di

import com.nyra.app.android.data.card.repository.CardRepositoryImpl
import com.nyra.app.android.data.checkin.repository.CheckInRepositoryImpl
import com.nyra.app.android.data.gentleaction.repository.GentleActionRepositoryImpl
import com.nyra.app.android.data.insight.repository.InsightRepositoryImpl
import com.nyra.app.android.data.journal.repository.JournalRepositoryImpl
import com.nyra.app.android.data.mood.repository.MoodRepositoryImpl
import com.nyra.app.android.data.notification.repository.NotificationRepositoryImpl
import com.nyra.app.android.data.preferences.repository.PreferenceRepositoryImpl
import com.nyra.app.android.data.preferences.repository.PreferencesRepositoryImpl
import com.nyra.app.android.domain.card.repository.CardRepository
import com.nyra.app.android.domain.checkin.repository.CheckInRepository
import com.nyra.app.android.domain.gentleaction.repository.GentleActionRepository
import com.nyra.app.android.domain.insight.repository.InsightRepository
import com.nyra.app.android.domain.journal.repository.JournalRepository
import com.nyra.app.android.domain.mood.repository.MoodRepository
import com.nyra.app.android.domain.notification.repository.NotificationRepository
import com.nyra.app.android.domain.preferences.repository.PreferenceRepository
import com.nyra.app.android.domain.settings.PreferencesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCheckInRepository(
        impl: CheckInRepositoryImpl
    ): CheckInRepository

    @Binds
    @Singleton
    abstract fun bindJournalRepository(
        impl: JournalRepositoryImpl
    ): JournalRepository

    @Binds
    @Singleton
    abstract fun bindMoodRepository(
        impl: MoodRepositoryImpl
    ): MoodRepository

    @Binds
    @Singleton
    abstract fun bindGentleActionRepository(
        impl: GentleActionRepositoryImpl
    ): GentleActionRepository

    @Binds
    @Singleton
    abstract fun bindCardRepository(
        impl: CardRepositoryImpl
    ): CardRepository

    @Binds
    @Singleton
    abstract fun bindInsightRepository(
        impl: InsightRepositoryImpl
    ): InsightRepository

    @Binds
    @Singleton
    abstract fun bindNotificationRepository(
        impl: NotificationRepositoryImpl
    ): NotificationRepository

    @Binds
    @Singleton
    abstract fun bindPreferenceRepository(
        impl: PreferenceRepositoryImpl
    ): PreferenceRepository

    @Binds
    @Singleton
    abstract fun bindPreferencesRepository(
        impl: PreferencesRepositoryImpl
    ): PreferencesRepository
}