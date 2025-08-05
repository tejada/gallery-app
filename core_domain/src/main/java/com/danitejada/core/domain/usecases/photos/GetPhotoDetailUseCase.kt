package com.danitejada.core.domain.usecases.photos

import com.danitejada.core.core.network.NetworkResult
import com.danitejada.core.domain.models.Photo
import com.danitejada.core.domain.repositories.PhotosRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPhotoDetailUseCase @Inject constructor(
  private val repository: PhotosRepository
) {
  operator fun invoke(photoId: Int): Flow<NetworkResult<Photo>> {
    return repository.getPhoto(photoId)
  }
}