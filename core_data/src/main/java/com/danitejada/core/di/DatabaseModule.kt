package com.danitejada.core.di

import android.content.Context
import androidx.room.Room
import com.danitejada.core.data.local.dao.PhotoDao
import com.danitejada.core.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

  @Provides
  @Singleton
  fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
    return Room.databaseBuilder(
      context,
      AppDatabase::class.java,
      "photos_database"
    ).build()
  }

  @Provides
  @Singleton
  fun providePhotoDao(appDatabase: AppDatabase): PhotoDao {
    return appDatabase.photoDao()
  }
}