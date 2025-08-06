package com.danitejada.gallery_app

/**
 * Represents the UI state controlling app initialization flow and navigation decision.
 */
sealed interface MainUiState {

  /**
   * The app is currently performing initial setup operations.
   */
  data object Loading : MainUiState

  /**
   * The app is ready and the initial navigation destination has been resolved.
   *
   * @property startDestination The destination to navigate to
   * (either [com.danitejada.gallery_app.navigation.ApiKeyDestination] or
   * [com.danitejada.gallery_app.navigation.PhotoListDestination]).
   */
  data class Ready(val startDestination: Any) : MainUiState
}