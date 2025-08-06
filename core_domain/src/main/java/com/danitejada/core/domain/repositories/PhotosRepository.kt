package com.danitejada.core.domain.repositories

import androidx.paging.PagingData
import com.danitejada.core.core.network.NetworkResult
import com.danitejada.core.domain.models.Photo
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for accessing photo data.
 */
interface PhotosRepository {

  /**
   * Retrieves a paginated list of photos.
   *
   * @return A [Flow] of [PagingData] containing [Photo] objects.
   */
  fun getPhotos(): Flow<PagingData<Photo>>

  /**
   * Retrieves details of a specific photo by its ID.
   *
   * @param photoId The ID of the photo to retrieve.
   * @return A [Flow] emitting a [NetworkResult] containing the [Photo] or an error.
   */
  fun getPhotoDetail(photoId: Int): Flow<NetworkResult<Photo>>
}