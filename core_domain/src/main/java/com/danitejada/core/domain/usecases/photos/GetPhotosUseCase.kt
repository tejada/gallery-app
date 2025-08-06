package com.danitejada.core.domain.usecases.photos

import androidx.paging.PagingData
import com.danitejada.core.domain.models.Photo
import com.danitejada.core.domain.repositories.PhotosRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
/**
 * Use case for retrieving a paginated list of photos.
 */
class GetPhotosUseCase @Inject constructor(
  private val photosRepository: PhotosRepository
) {

  /**
   * Fetches a paginated list of photos from the repository.
   *
   * @return A [Flow] of [PagingData] containing [Photo] objects.
   */
  operator fun invoke(): Flow<PagingData<Photo>> {
    return photosRepository.getPhotos()
  }
}