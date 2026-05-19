package com.nyra.app.android.di

import android.content.Context
import androidx.room.Room
import com.nyra.app.android.core.database.NyraDatabase
import com.nyra.app.android.core.database.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideNyraDatabase(
        @ApplicationContext context: Context
    ): NyraDatabase {
        return Room.databaseBuilder(
            context,
            NyraDatabase::class.java,
            "nyra-database"
        ).build()
    }

    @Provides
    fun provideMoodDao(database: NyraDatabase): MoodDao = database.moodDao()

    @Provides
    fun provideCheckInDao(database: NyraDatabase): CheckInDao = database.checkInDao()

    @Provides
    fun provideJournalDao(database: NyraDatabase): JournalDao = database.journalDao()

    @Provides
    fun provideGentleActionDao(database: NyraDatabase): GentleActionDao = database.gentleActionDao()

    @Provides
    fun provideCardDao(database: NyraDatabase): CardDao = database.cardDao()

    @Provides
    fun provideInsightDao(database: NyraDatabase): InsightDao = database.insightDao()

    @Provides
    fun provideNotificationDao(database: NyraDatabase): NotificationDao = database.notificationDao()
}