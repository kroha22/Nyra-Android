package com.nyra.app.android.di

import com.nyra.app.android.core.ui_state.resolver.NyraUiStateConfigResolver
import com.nyra.app.android.core.ui_state.resolver.NyraUiStateConfigResolverImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NyraUiStateModule {

    @Binds
    @Singleton
    abstract fun bindNyraUiStateConfigResolver(
        impl: NyraUiStateConfigResolverImpl
    ): NyraUiStateConfigResolver
}
