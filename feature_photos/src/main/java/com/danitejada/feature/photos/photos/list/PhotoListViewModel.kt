package com.danitejada.feature.photos.photos.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.danitejada.core.domain.models.Photo
import com.danitejada.core.domain.usecases.photos.GetPhotosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * ViewModel for managing the photo list data.
 *
 * @param getPhotosUseCase Use case for fetching paginated photos.
 */
@HiltViewModel
class PhotoListViewModel @Inject constructor(
  getPhotosUseCase: GetPhotosUseCase
) : ViewModel() {

  /**
   * Flow of paginated photos, cached in the ViewModel scope.
   */
  val photos: Flow<PagingData<Photo>> = getPhotosUseCase().cachedIn(viewModelScope)
}