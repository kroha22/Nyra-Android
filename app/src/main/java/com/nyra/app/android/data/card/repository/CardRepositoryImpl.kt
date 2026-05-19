package com.nyra.app.android.data.card.repository

import com.nyra.app.android.core.database.dao.CardDao
import com.nyra.app.android.core.model.CardTemplate
import com.nyra.app.android.data.card.mapper.toDomain
import com.nyra.app.android.data.card.mapper.toEntity
import com.nyra.app.android.domain.card.repository.CardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CardRepositoryImpl @Inject constructor(
    private val dao: CardDao
) : CardRepository {

    override fun observeActiveCards(): Flow<List<CardTemplate>> {
        return dao.getActiveCards().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun syncCards(cards: List<CardTemplate>) {
        dao.insertCards(cards.map { it.toEntity() })
    }
}