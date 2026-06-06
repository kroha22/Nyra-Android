package com.nyra.app.android.core.content.loader

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import javax.inject.Inject

interface NyraContentAssetReader {
    fun readText(path: String): NyraAssetReadResult
}

sealed interface NyraAssetReadResult {
    data class Success(val content: String) : NyraAssetReadResult
    data class Failure(val path: String, val reason: String) : NyraAssetReadResult
}

class AndroidNyraContentAssetReader @Inject constructor(
    @ApplicationContext private val context: Context
) : NyraContentAssetReader {

    override fun readText(path: String): NyraAssetReadResult = try {
        context.assets.open(path).use { stream ->
            NyraAssetReadResult.Success(stream.bufferedReader().use { it.readText() })
        }
    } catch (exception: IOException) {
        NyraAssetReadResult.Failure(
            path = path,
            reason = exception.message ?: "Asset file is missing or unreadable"
        )
    }
}
