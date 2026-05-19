package com.nyra.app.android.data.mood.repository

import com.nyra.app.android.core.database.dao.MoodDao
import com.nyra.app.android.core.model.MoodState
import com.nyra.app.android.core.model.PresenceState
import com.nyra.app.android.data.mood.mapper.toDomain
import com.nyra.app.android.data.mood.mapper.toEntity
import com.nyra.app.android.domain.mood.repository.MoodRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MoodRepositoryImpl @Inject constructor(
    private val dao: MoodDao
) : MoodRepository {

    override fun observeActiveMoodStates(): Flow<List<MoodState>> {
        return dao.getActiveMoodStates().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun observePresenceStates(): Flow<List<PresenceState>> {
        return dao.getPresenceStates().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun syncMoodStates(moods: List<MoodState>) {
        dao.insertMoodStates(moods.map { it.toEntity() })
    }

    override suspend fun syncPresenceStates(states: List<PresenceState>) {
        dao.insertPresenceStates(states.map { it.toEntity() })
    }
}