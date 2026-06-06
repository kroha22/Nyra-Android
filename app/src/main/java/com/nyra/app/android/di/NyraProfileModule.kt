package com.nyra.app.android.di

import com.nyra.app.android.core.profile.synthesis.NyraProfileSynthesizer
import com.nyra.app.android.core.profile.synthesis.NyraProfileSynthesizerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NyraProfileModule {

    @Binds
    @Singleton
    abstract fun bindNyraProfileSynthesizer(
        impl: NyraProfileSynthesizerImpl
    ): NyraProfileSynthesizer
}
