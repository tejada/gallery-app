package com.danitejada.feature.photos.photos.list

import com.danitejada.core.domain.models.Photo

/**
 * Represents the UI state for the photo list screen.
 */
sealed interface PhotoListUiState {

  /**
   * Indicates the screen is in a loading state while fetching the initial photo list.
   */
  data object Loading : PhotoListUiState

  /**
   * Indicates the photo list was successfully loaded.
   *
   * @param photos The list of photos to display.
   * @param hasNextPage Indicates if there are more pages of photos to load.
   * @param isLoadingMore Indicates if additional photos are being loaded.
   * @param isRefreshing Indicates if the photo list is being refreshed.
   */
  data class Success(
    val photos: List<Photo>,
    val hasNextPage: Boolean,
    val isLoadingMore: Boolean = false,
    val isRefreshing: Boolean = false
  ) : PhotoListUiState

  /**
   * Indicates the API call was successful, but there are no photos to display.
   */
  data object Empty : PhotoListUiState

  /**
   * Indicates an error occurred while fetching the photo list.
   *
   * @param message The error message to display.
   */
  data class Error(val message: String) : PhotoListUiState
}