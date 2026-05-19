package com.nyra.app.android.domain.mood.repository

import com.nyra.app.android.core.model.MoodState
import com.nyra.app.android.core.model.PresenceState
import kotlinx.coroutines.flow.Flow

interface MoodRepository {
    fun observeActiveMoodStates(): Flow<List<MoodState>>
    fun observePresenceStates(): Flow<List<PresenceState>>
    suspend fun syncMoodStates(moods: List<MoodState>)
    suspend fun syncPresenceStates(states: List<PresenceState>)
}