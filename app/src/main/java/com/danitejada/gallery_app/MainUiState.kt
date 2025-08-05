package com.danitejada.gallery_app

sealed interface MainUiState {
  data object Loading : MainUiState
  data class Ready(val startDestination: Any) : MainUiState
}