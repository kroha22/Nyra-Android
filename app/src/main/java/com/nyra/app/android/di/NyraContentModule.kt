package com.nyra.app.android.di

import com.nyra.app.android.core.content.loader.AndroidNyraContentAssetReader
import com.nyra.app.android.core.content.loader.NyraContentAssetReader
import com.nyra.app.android.core.content.repository.NyraContentRepository
import com.nyra.app.android.core.content.repository.NyraContentRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.serialization.json.Json

@Module
@InstallIn(SingletonComponent::class)
abstract class NyraContentBindingsModule {

    @Binds
    @Singleton
    abstract fun bindNyraContentAssetReader(
        impl: AndroidNyraContentAssetReader
    ): NyraContentAssetReader

    @Binds
    @Singleton
    abstract fun bindNyraContentRepository(
        impl: NyraContentRepositoryImpl
    ): NyraContentRepository
}

@Module
@InstallIn(SingletonComponent::class)
object NyraContentJsonModule {

    @Provides
    @Singleton
    fun provideNyraContentJson(): Json = Json {
        ignoreUnknownKeys = true
        isLenient = false
    }
}
