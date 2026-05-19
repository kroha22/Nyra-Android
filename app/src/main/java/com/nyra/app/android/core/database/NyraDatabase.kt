package com.nyra.app.android.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nyra.app.android.core.database.dao.*
import com.nyra.app.android.core.database.entity.*
import com.nyra.app.android.core.database.util.NyraConverters

@Database(
    entities = [
        MoodStateEntity::class,
        PresenceStateEntity::class,
        CheckInEntryEntity::class,
        JournalEntryEntity::class,
        GentleActionEntity::class,
        CardTemplateEntity::class,
        InsightSnapshotEntity::class,
        NotificationScheduleEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(NyraConverters::class)
abstract class NyraDatabase : RoomDatabase() {
    abstract fun moodDao(): MoodDao
    abstract fun checkInDao(): CheckInDao
    abstract fun journalDao(): JournalDao
    abstract fun gentleActionDao(): GentleActionDao
    abstract fun cardDao(): CardDao
    abstract fun insightDao(): InsightDao
    abstract fun notificationDao(): NotificationDao
}