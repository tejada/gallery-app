package com.danitejada.core.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.danitejada.core.core.Constants
import com.danitejada.core.core.network.NetworkResult
import com.danitejada.core.data.local.dao.PhotoDao
import com.danitejada.core.data.mappers.PhotoMapper
import com.danitejada.core.data.remote.api.PhotosApi
import com.danitejada.core.data.remote.paging.PhotosPagingSource
import com.danitejada.core.domain.models.Photo
import com.danitejada.core.domain.repositories.PhotosRepository
import com.danitejada.core.domain.repositories.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of [PhotosRepository] for fetching photo data from a remote API.
 */
@Singleton
class PhotosRepositoryImpl @Inject constructor(
  private val api: PhotosApi,
  private val dao: PhotoDao,
  private val mapper: PhotoMapper,
  private val settingsRepository: SettingsRepository
) : PhotosRepository {

  /**
   * Retrieves a paginated list of photos.
   *
   * @return A [Flow] of [PagingData] containing [Photo] objects.
   */
  override fun getPhotos(): Flow<PagingData<Photo>> {
    return Pager(
      config = PagingConfig(
        pageSize = Constants.PAGE_SIZE,
        enablePlaceholders = false
      ),
      pagingSourceFactory = {
        PhotosPagingSource(api, settingsRepository, mapper, dao)
      }
    ).flow
  }

  /**
   * Fetches from [PhotosApi], caches via [PhotoDao], and uses [SettingsRepository] for API key.
   *
   * @param photoId The ID of the photo to retrieve.
   * @return A [Flow] emitting a [NetworkResult] containing the [Photo] or an error.
   */
  override fun getPhotoDetail(photoId: Int): Flow<NetworkResult<Photo>> = flow {
    // Emit cached data first, if available
    val cachedPhotoEntity = dao.getPhotoById(photoId)
    try {
      emit(NetworkResult.Loading)

      // Emit cached data first, if available
      if (cachedPhotoEntity != null) {
        emit(NetworkResult.Success(mapper.mapEntityToDomain(cachedPhotoEntity)))
      }

      // Fetch from network to get the latest data
      val apiKey = settingsRepository.getApiKey()?.value
      if (apiKey.isNullOrBlank()) {
        if (cachedPhotoEntity == null) {
          emit(NetworkResult.Error("API key not found and no cached data available."))
        }
        return@flow // Stop if no API key and we've already emitted cache
      }

      val response = api.getPhoto(apiKey = apiKey, id = photoId)
      val freshPhoto = mapper.mapDtoToDomain(response)

      // Save fresh data to the database
      dao.insertPhotos(listOf(mapper.mapDomainToEntity(freshPhoto)))

      // Emit the fresh data
      val updatedPhotoEntity = dao.getPhotoById(photoId)
      if (updatedPhotoEntity != null) {
        emit(NetworkResult.Success(mapper.mapEntityToDomain(updatedPhotoEntity)))
      }

    } catch (e: Exception) {
      // If the network fails, the UI still has the cached version.
      // Only emit an error if there's no cached data at all.
      if (cachedPhotoEntity == null) {
        emit(NetworkResult.Error(e.message ?: "Unknown error occurred"))
      }
    }
  }
}