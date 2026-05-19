package com.nyra.app.android.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nyra.app.android.core.model.PresenceCode

@Entity(tableName = "presence_states")
data class PresenceStateEntity(
    @PrimaryKey val id: String,
    val code: PresenceCode,
    val title: String,
    val description: String?,
    val shapeToken: String,
    val glowToken: String,
    val textureToken: String,
    val motionToken: String,
    val backgroundToken: String,
    val brightness: Int,
    val density: Int,
    val expansion: Int,
    val warmth: Int
)