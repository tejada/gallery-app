package com.danitejada.feature.photos.photos.detail

import com.danitejada.core.domain.models.Photo

sealed interface PhotoDetailUiState {
  // The screen is loading for the first time
  data object Loading : PhotoDetailUiState

  // The screen has successfully loaded the photo
  data class Success(val photo: Photo?) : PhotoDetailUiState

  // An error occurred while fetching data
  data class Error(val message: String?) : PhotoDetailUiState
}