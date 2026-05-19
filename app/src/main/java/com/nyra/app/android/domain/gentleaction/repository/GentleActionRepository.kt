package com.nyra.app.android.domain.gentleaction.repository

import com.nyra.app.android.core.model.GentleAction
import kotlinx.coroutines.flow.Flow

interface GentleActionRepository {
    fun observeActiveActions(): Flow<List<GentleAction>>
    suspend fun getActionById(id: String): GentleAction?
    suspend fun syncActions(actions: List<GentleAction>)
}