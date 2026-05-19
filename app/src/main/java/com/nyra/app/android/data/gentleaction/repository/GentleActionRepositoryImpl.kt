package com.nyra.app.android.data.gentleaction.repository

import com.nyra.app.android.core.database.dao.GentleActionDao
import com.nyra.app.android.core.model.GentleAction
import com.nyra.app.android.data.gentleaction.mapper.toDomain
import com.nyra.app.android.data.gentleaction.mapper.toEntity
import com.nyra.app.android.domain.gentleaction.repository.GentleActionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GentleActionRepositoryImpl @Inject constructor(
    private val dao: GentleActionDao
) : GentleActionRepository {

    override fun observeActiveActions(): Flow<List<GentleAction>> {
        return dao.getActiveActions().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getActionById(id: String): GentleAction? {
        return dao.getActionById(id)?.toDomain()
    }

    override suspend fun syncActions(actions: List<GentleAction>) {
        dao.insertActions(actions.map { it.toEntity() })
    }
}