package com.danitejada.feature.photos.photos.list

import com.danitejada.core.domain.models.Photo

sealed interface PhotoListUiState {
  // The screen is loading for the first time
  data object Loading : PhotoListUiState

  // The screen has successfully loaded a list of photos
  data class Success(
    val photos: List<Photo>,
    val hasNextPage: Boolean,
    val isLoadingMore: Boolean = false,
    val isRefreshing: Boolean = false
  ) : PhotoListUiState

  // The API call was successful, but there are no photos to display
  data object Empty : PhotoListUiState

  // An error occurred while fetching data
  data class Error(val message: String) : PhotoListUiState
}