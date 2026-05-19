package com.nyra.app.android.core.database.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nyra.app.android.core.model.CardActionType
import com.nyra.app.android.core.model.CardType

@Entity(tableName = "card_templates")
data class CardTemplateEntity(
    @PrimaryKey val id: String,
    val type: CardType,
    val title: String,
    val subtitle: String?,
    val iconToken: String?,
    val visualToken: String?,
    @Embedded(prefix = "action_") val action: CardActionEntity,
    val priority: Int,
    val isActive: Boolean
)

data class CardActionEntity(
    val type: CardActionType,
    val payload: Map<String, String>
)