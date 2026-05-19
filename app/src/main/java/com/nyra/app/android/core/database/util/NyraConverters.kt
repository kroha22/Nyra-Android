package com.nyra.app.android.core.database.util

import androidx.room.TypeConverter
import com.nyra.app.android.core.model.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class NyraConverters {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    @TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it, formatter) }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): String? {
        return date?.format(formatter)
    }

    @TypeConverter
    fun fromLocalDate(value: String?): LocalDate? {
        return value?.let { LocalDate.parse(it, dateFormatter) }
    }

    @TypeConverter
    fun localDateToTimestamp(date: LocalDate?): String? {
        return date?.format(dateFormatter)
    }

    @TypeConverter
    fun fromStringList(value: String): List<String> {
        return if (value.isBlank()) emptyList() else value.split(",")
    }

    @TypeConverter
    fun toStringList(list: List<String>): String {
        return list.joinToString(",")
    }

    @TypeConverter
    fun fromIntList(value: String): List<Int> {
        return if (value.isBlank()) emptyList() else value.split(",").map { it.toInt() }
    }

    @TypeConverter
    fun toIntList(list: List<Int>): String {
        return list.joinToString(",")
    }

    @TypeConverter
    fun fromMoodCodeList(value: String): List<MoodCode> {
        return if (value.isBlank()) emptyList() else value.split(",").map { MoodCode.valueOf(it) }
    }

    @TypeConverter
    fun toMoodCodeList(list: List<MoodCode>): String {
        return list.joinToString(",") { it.name }
    }

    @TypeConverter
    fun fromTimeOfDayList(value: String): List<TimeOfDay> {
        return if (value.isBlank()) emptyList() else value.split(",").map { TimeOfDay.valueOf(it) }
    }

    @TypeConverter
    fun toTimeOfDayList(list: List<TimeOfDay>): String {
        return list.joinToString(",") { it.name }
    }

    @TypeConverter
    fun fromStringMap(value: String): Map<String, String> {
        return try {
            Json.decodeFromString(value)
        } catch (e: Exception) {
            emptyMap()
        }
    }

    @TypeConverter
    fun toStringMap(map: Map<String, String>): String {
        return Json.encodeToString(map)
    }

    @TypeConverter
    fun fromMoodCodeIntMap(value: String): Map<MoodCode, Int> {
        return try {
            val stringMap: Map<String, Int> = Json.decodeFromString(value)
            stringMap.mapKeys { MoodCode.valueOf(it.key) }
        } catch (e: Exception) {
            emptyMap()
        }
    }

    @TypeConverter
    fun toMoodCodeIntMap(map: Map<MoodCode, Int>): String {
        val stringMap = map.mapKeys { it.key.name }
        return Json.encodeToString(stringMap)
    }

    @TypeConverter
    fun fromStringMoodCodeMap(value: String): Map<String, MoodCode> {
        return try {
            val stringMap: Map<String, String> = Json.decodeFromString(value)
            stringMap.mapValues { MoodCode.valueOf(it.value) }
        } catch (e: Exception) {
            emptyMap()
        }
    }

    @TypeConverter
    fun toStringMoodCodeMap(map: Map<String, MoodCode>): String {
        val stringMap = map.mapValues { it.value.name }
        return Json.encodeToString(stringMap)
    }
}