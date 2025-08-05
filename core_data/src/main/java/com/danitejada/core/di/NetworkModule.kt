package com.danitejada.core.di

import com.danitejada.core.core.network.HttpClientFactory
import com.danitejada.core.data.remote.api.PhotosApi
import com.danitejada.core.data.remote.service.PhotosService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

  @Provides
  @Singleton
  fun provideJson(): Json = Json {
    ignoreUnknownKeys = true
    isLenient = true
    encodeDefaults = true
  }

  @Provides
  @Singleton
  fun provideHttpClient(json: Json): HttpClient {
    return HttpClientFactory().create(json)
  }

  @Provides
  @Singleton
  fun providePhotosApi(httpClient: HttpClient): PhotosApi {
    return PhotosService(httpClient)
  }
}