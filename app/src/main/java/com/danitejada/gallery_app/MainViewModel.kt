package com.danitejada.gallery_app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danitejada.core.domain.usecases.settings.HasApiKeyUseCase
import com.danitejada.core.domain.usecases.settings.SeedInitialApiKeyUseCase
import com.danitejada.gallery_app.navigation.ApiKeyDestination
import com.danitejada.gallery_app.navigation.PhotoListDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
  private val seedInitialApiKeyUseCase: SeedInitialApiKeyUseCase,
  private val hasApiKeyUseCase: HasApiKeyUseCase
) : ViewModel() {

  private val _uiState = MutableStateFlow<MainUiState>(MainUiState.Loading)
  val uiState = _uiState.asStateFlow()

  init {
    checkApiKey()
  }

  private fun checkApiKey() {
    viewModelScope.launch {
      // First, ensure the initial key is seeded if it hasn't been already
      seedInitialApiKeyUseCase()

      // Then, check if a valid key exists to determine the start destination
      val startDestination = if (hasApiKeyUseCase()) {
        PhotoListDestination
      } else {
        ApiKeyDestination
      }
      _uiState.value = MainUiState.Ready(startDestination)
    }
  }
}