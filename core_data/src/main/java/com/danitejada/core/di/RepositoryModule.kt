package com.danitejada.core.di

import com.danitejada.core.data.repositories.PhotosRepositoryImpl
import com.danitejada.core.data.repositories.SettingsRepositoryImpl
import com.danitejada.core.domain.repositories.PhotosRepository
import com.danitejada.core.domain.repositories.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

  @Binds
  @Singleton
  abstract fun bindPhotosRepository(
    photosRepositoryImpl: PhotosRepositoryImpl
  ): PhotosRepository

  @Binds
  @Singleton
  abstract fun bindSettingsRepository(
    settingsRepositoryImpl: SettingsRepositoryImpl
  ): SettingsRepository
}