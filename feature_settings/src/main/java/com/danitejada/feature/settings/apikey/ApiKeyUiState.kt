package com.danitejada.feature.settings.apikey

import androidx.annotation.StringRes

/**
 * Represents the UI state of the API key input screen.
 */
sealed interface ApiKeyUiState {
  /** Loading state while saving the key */
  data object Loading : ApiKeyUiState

  /** State when the key is successfully saved */
  data class Success(val apiKey: String) : ApiKeyUiState

  /** Error state with string resource ID for message */
  data class Error(@StringRes val messageResId: Int) : ApiKeyUiState

  /** Default idle state */
  data class Idle(val apiKey: String) : ApiKeyUiState
}