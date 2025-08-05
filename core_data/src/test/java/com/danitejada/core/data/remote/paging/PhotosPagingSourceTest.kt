package com.danitejada.core.data.remote.paging

import androidx.paging.PagingSource
import com.danitejada.core.data.local.dao.PhotoDao
import com.danitejada.core.data.mappers.PhotoMapper
import com.danitejada.core.data.remote.api.PhotosApi
import com.danitejada.core.data.remote.dto.PhotoDto
import com.danitejada.core.data.remote.dto.PhotosResponseDto
import com.danitejada.core.domain.models.ApiKey
import com.danitejada.core.domain.repositories.SettingsRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.io.IOException

class PhotosPagingSourceTest {

  private val photosApi: PhotosApi = mock()
  private val settingsRepository: SettingsRepository = mock()
  private val photoDao: PhotoDao = mock()
  private val photoMapper = PhotoMapper() // Using real mapper

  private lateinit var photosPagingSource: PhotosPagingSource

  private val mockPhotoDto = PhotoDto(id = 1, alt = "A beautiful landscape")
  private val mockPhoto = photoMapper.mapDtoToDomain(mockPhotoDto)

  @Before
  fun setup() {
    photosPagingSource = PhotosPagingSource(photosApi, settingsRepository, photoMapper, photoDao)
  }

  @Test
  fun `load returns page when successful`() = runBlocking {
    // Given a valid API key and a successful API response
    whenever(settingsRepository.getApiKey()).thenReturn(ApiKey("valid_key", true))
    val response = PhotosResponseDto(page = 1, perPage = 1, photos = listOf(mockPhotoDto), totalResults = 1, nextPage = "next_page_url")
    whenever(photosApi.getPhotos(any(), any(), any())).thenReturn(response)

    // When load is called
    val result = photosPagingSource.load(
      PagingSource.LoadParams.Refresh(key = 1, loadSize = 1, placeholdersEnabled = false)
    )

    // Then the result is a Page with the correct data
    assertTrue(result is PagingSource.LoadResult.Page)
    val page = result as PagingSource.LoadResult.Page
    assertEquals(listOf(mockPhoto), page.data)
    assertEquals(2, page.nextKey) // nextKey should be page + 1
    assertEquals(null, page.prevKey)
  }

  @Test
  fun `load returns error when API key is not found`() = runBlocking {
    // Given the API key is null
    whenever(settingsRepository.getApiKey()).thenReturn(null)

    // When load is called
    val result = photosPagingSource.load(
      PagingSource.LoadParams.Refresh(key = 1, loadSize = 1, placeholdersEnabled = false)
    )

    // Then the result is an Error
    assertTrue(result is PagingSource.LoadResult.Error)
    val error = result as PagingSource.LoadResult.Error
    assertEquals("API key not found", error.throwable.message)
  }

  @Test
  fun `load returns error on network exception`() = runBlocking {
    // Given a valid API key but the API throws an exception
    whenever(settingsRepository.getApiKey()).thenReturn(ApiKey("valid_key", true))
    val ioException = IOException("Network failed")
    whenever(photosApi.getPhotos(any(), any(), any())).thenThrow(ioException)

    // When load is called
    val result = photosPagingSource.load(
      PagingSource.LoadParams.Refresh(key = 1, loadSize = 1, placeholdersEnabled = false)
    )

    // Then the result is an Error
    assertTrue(result is PagingSource.LoadResult.Error)
    assertEquals(ioException, (result as PagingSource.LoadResult.Error).throwable)
  }
}