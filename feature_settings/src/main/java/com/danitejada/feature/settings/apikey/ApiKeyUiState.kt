package com.danitejada.feature.settings.apikey

import androidx.annotation.StringRes

/**
 * Represents the UI state of the API key input screen.
 */
sealed interface ApiKeyUiState {
  /**
  * Indicates the screen is in a loading state while saving the API key.
  */
  data object Loading : ApiKeyUiState

  /**
   * Indicates the API key was successfully saved.
   *
   * @param apiKey The saved API key.
   */
  data class Success(val apiKey: String) : ApiKeyUiState

  /**
   * Indicates an error occurred while saving the API key.
   *
   * @param messageResId The resource ID for the error message to display.
   */
  data class Error(@StringRes val messageResId: Int) : ApiKeyUiState

  /**
   * Represents the default idle state of the screen.
   *
   * @param apiKey The current API key value in the input field.
   */
  data class Idle(val apiKey: String) : ApiKeyUiState
}