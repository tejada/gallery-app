package com.danitejada.feature.settings.apikey

import androidx.annotation.StringRes

sealed interface ApiKeyUiState {
  // The screen is loading
  data object Loading : ApiKeyUiState

  // The screen has successfully loaded the API key
  data class Success(val apiKey: String) : ApiKeyUiState

  // An error occurred
  data class Error(@StringRes val messageResId: Int) : ApiKeyUiState

  // An idle state while we wait for the user input
  data object Idle : ApiKeyUiState
}