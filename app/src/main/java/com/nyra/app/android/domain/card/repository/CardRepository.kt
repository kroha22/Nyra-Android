package com.nyra.app.android.domain.card.repository

import com.nyra.app.android.core.model.CardTemplate
import kotlinx.coroutines.flow.Flow

interface CardRepository {
    fun observeActiveCards(): Flow<List<CardTemplate>>
    suspend fun syncCards(cards: List<CardTemplate>)
}