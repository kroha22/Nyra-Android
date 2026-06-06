package com.nyra.app.android.core.content.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive

@Serializable(with = LocalizedTextSerializer::class)
data class LocalizedText(
    val en: String,
    val ru: String
) {
    val isComplete: Boolean
        get() = en.isNotBlank() && ru.isNotBlank()
}

object LocalizedTextSerializer : KSerializer<LocalizedText> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("LocalizedText", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): LocalizedText {
        val jsonDecoder = decoder as? JsonDecoder
            ?: throw SerializationException("LocalizedText requires JSON decoding")
        return when (val element = jsonDecoder.decodeJsonElement()) {
            is JsonObject -> {
                val en = element.stringValue("en")
                val ru = element.stringValue("ru")
                LocalizedText(en = en, ru = ru)
            }
            is JsonPrimitive -> {
                val value = element.contentOrNull.orEmpty()
                LocalizedText(en = value, ru = value)
            }
            else -> throw SerializationException("LocalizedText must be a string or object")
        }
    }

    override fun serialize(encoder: Encoder, value: LocalizedText) {
        encoder.encodeString(value.en)
    }

    private fun JsonObject.stringValue(key: String): String =
        this[key]?.jsonPrimitive?.contentOrNull.orEmpty()
}
