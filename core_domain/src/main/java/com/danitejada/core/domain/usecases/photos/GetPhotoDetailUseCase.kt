package com.danitejada.core.domain.usecases.photos

import com.danitejada.core.core.network.NetworkResult
import com.danitejada.core.domain.models.Photo
import com.danitejada.core.domain.repositories.PhotosRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for retrieving details of a specific photo.
 */
class GetPhotoDetailUseCase @Inject constructor(
  private val photosRepository: PhotosRepository
) {

  /**
   * Fetches the details of a photo by its ID.
   *
   * @param photoId The ID of the photo to retrieve.
   * @return A [Flow] emitting a [NetworkResult] containing the [Photo] or an error.
   */
  operator fun invoke(photoId: Int): Flow<NetworkResult<Photo>> {
    return photosRepository.getPhotoDetail(photoId)
  }
}