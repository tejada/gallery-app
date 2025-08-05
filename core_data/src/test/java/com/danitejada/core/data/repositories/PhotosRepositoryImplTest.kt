package com.danitejada.core.data.repositories

import app.cash.turbine.test
import com.danitejada.core.core.network.NetworkResult
import com.danitejada.core.data.local.dao.PhotoDao
import com.danitejada.core.data.local.entities.PhotoEntity
import com.danitejada.core.data.mappers.PhotoMapper
import com.danitejada.core.data.remote.api.PhotosApi
import com.danitejada.core.data.remote.dto.PhotoDto
import com.danitejada.core.domain.models.ApiKey
import com.danitejada.core.domain.repositories.SettingsRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class PhotosRepositoryImplTest {

  private val api: PhotosApi = mock()
  private val dao: PhotoDao = mock()
  private val mapper: PhotoMapper = PhotoMapper() // Using real mapper
  private val settingsRepository: SettingsRepository = mock()

  private val repository = PhotosRepositoryImpl(api, dao, mapper, settingsRepository)

  private val photoId = 123
  private val cachedEntity = PhotoEntity(photoId, 100, 100, "url", "Cached Dani", "url", 1L, "#FFFFFF", "thumb", "tiny", "large", "alt")
  private val networkDto = PhotoDto(id = photoId, width = 200, height = 200, photographer = "Network Dani")
  private val freshEntity = PhotoEntity(photoId, 200, 200, null, "Network Dani", null, null, null, null, null, null, null)

  @Test
  fun `getPhoto emits cache first, then fresh network data`() = runBlocking {
    // Given the database has a cached version
    whenever(dao.getPhotoById(photoId)).thenReturn(cachedEntity, freshEntity) // First call returns cache, second returns fresh
    // And the network will provide a fresh version
    whenever(settingsRepository.getApiKey()).thenReturn(ApiKey("valid_key", true))
    whenever(api.getPhoto("valid_key", photoId)).thenReturn(networkDto)

    // When getPhoto is called
    repository.getPhoto(photoId).test {
      // Then it emits Loading first
      assertEquals(NetworkResult.Loading, awaitItem())

      // Then it emits the cached data
      val cachedResult = awaitItem() as NetworkResult.Success
      assertEquals("Cached Dani", cachedResult.data.photographer)

      // Then it emits the fresh network data
      val freshResult = awaitItem() as NetworkResult.Success
      assertEquals("Network Dani", freshResult.data.photographer)

      // Ensure we inserted the fresh data into the database
      verify(dao).insertPhotos(listOf(mapper.mapDomainToEntity(mapper.mapDtoToDomain(networkDto))))

      awaitComplete()
    }
  }

  @Test
  fun `getPhoto emits only cache if network fails`() = runBlocking {
    // Given the database has a cached version
    whenever(dao.getPhotoById(photoId)).thenReturn(cachedEntity)
    // And the network call will fail
    whenever(settingsRepository.getApiKey()).thenReturn(ApiKey("valid_key", true))
    whenever(api.getPhoto("valid_key", photoId)).thenThrow(RuntimeException("Network Error"))

    // When getPhoto is called
    repository.getPhoto(photoId).test {
      // Then it emits Loading
      assertEquals(NetworkResult.Loading, awaitItem())
      // Then it emits the cached data
      val cachedResult = awaitItem() as NetworkResult.Success
      assertEquals("Cached Dani", cachedResult.data.photographer)
      // And it completes without emitting an error because cache was available
      awaitComplete()
    }
  }

  @Test
  fun `getPhoto emits error if network fails and no cache exists`() = runBlocking {
    // Given the database is empty
    whenever(dao.getPhotoById(photoId)).thenReturn(null)
    // And the network call will fail
    whenever(settingsRepository.getApiKey()).thenReturn(ApiKey("valid_key", true))
    whenever(api.getPhoto("valid_key", photoId)).thenThrow(RuntimeException("Network Error"))

    // When getPhoto is called
    repository.getPhoto(photoId).test {
      // Then it emits Loading
      assertEquals(NetworkResult.Loading, awaitItem())
      // Then it emits an Error
      val errorResult = awaitItem() as NetworkResult.Error
      assertTrue(errorResult.message.contains("Network Error"))

      awaitComplete()
    }
  }

  @Test
  fun `getPhoto emits error if no API key and no cache`() = runBlocking {
    // Given the database is empty and no API key is available
    whenever(dao.getPhotoById(photoId)).thenReturn(null)
    whenever(settingsRepository.getApiKey()).thenReturn(null)

    // When getPhoto is called
    repository.getPhoto(photoId).test {
      // Then it emits Loading
      assertEquals(NetworkResult.Loading, awaitItem())
      // Then it emits an Error
      val errorResult = awaitItem() as NetworkResult.Error
      assertEquals("API key not found and no cached data available.", errorResult.message)

      // And it does not attempt to make a network call
      verify(api, never()).getPhoto(any(), any())

      awaitComplete()
    }
  }
}