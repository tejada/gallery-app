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

@HiltViewModel
class PhotoListViewModel @Inject constructor(
  useCase: GetPhotosUseCase
) : ViewModel() {
  val photos: Flow<PagingData<Photo>> = useCase().cachedIn(viewModelScope)
}