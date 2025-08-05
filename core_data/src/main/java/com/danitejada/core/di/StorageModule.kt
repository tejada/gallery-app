package com.danitejada.core.di

import android.content.Context
import com.danitejada.core.data.local.preferences.SecurePreferencesDataSource
import com.danitejada.core.data.security.CryptoManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StorageModule {

  @Provides
  @Singleton
  fun provideCryptoManager(): CryptoManager {
    return CryptoManager()
  }

  @Provides
  @Singleton
  fun providePreferencesDataSource(
    @ApplicationContext context: Context,
    cryptoManager: CryptoManager
  ): SecurePreferencesDataSource {
    return SecurePreferencesDataSource(context, cryptoManager)
  }
}