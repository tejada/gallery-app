package com.danitejada.feature.photos.photos.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danitejada.core.core.network.NetworkResult
import com.danitejada.core.domain.usecases.photos.GetPhotoDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class PhotoDetailViewModel @Inject constructor(
  private val useCase: GetPhotoDetailUseCase
) : ViewModel() {

  private val _uiState = MutableStateFlow<PhotoDetailUiState>(PhotoDetailUiState.Loading)
  val uiState: StateFlow<PhotoDetailUiState> = _uiState.asStateFlow()

  fun loadPhoto(photoId: Int) {
    useCase(photoId).onEach { result ->
      val newState = when (result) {
        is NetworkResult.Success -> PhotoDetailUiState.Success(result.data)
        is NetworkResult.Error -> PhotoDetailUiState.Error(result.message)
        is NetworkResult.Loading -> PhotoDetailUiState.Loading
      }
      _uiState.value = newState
    }.launchIn(viewModelScope)
  }
}