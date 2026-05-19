package com.nyra.app.android.data.cards

import com.nyra.app.android.core.catalog.NyraContentCatalog
import com.nyra.app.android.core.model.CardTemplate
import com.nyra.app.android.core.model.CardType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NyraCardCatalog @Inject constructor() {
    fun getByType(type: CardType): List<CardTemplate> {
        return NyraContentCatalog.cardTemplates.filter { it.type == type }
    }

    fun getById(id: String): CardTemplate {
        return NyraContentCatalog.cardTemplates.find { it.id == id }
            ?: NyraContentCatalog.cardTemplates.first()
    }
}