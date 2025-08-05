package com.danitejada.core.domain.repositories

import androidx.paging.PagingData
import com.danitejada.core.core.network.NetworkResult
import com.danitejada.core.domain.models.Photo
import kotlinx.coroutines.flow.Flow

interface PhotosRepository {
  fun getPhotos(): Flow<PagingData<Photo>>
  fun getPhoto(photoId: Int): Flow<NetworkResult<Photo>>
}