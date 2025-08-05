package com.danitejada.core.domain.usecases.photos

import androidx.paging.PagingData
import com.danitejada.core.domain.models.Photo
import com.danitejada.core.domain.repositories.PhotosRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPhotosUseCase @Inject constructor(
  private val repository: PhotosRepository
) {
  operator fun invoke(): Flow<PagingData<Photo>> {
    return repository.getPhotos()
  }
}