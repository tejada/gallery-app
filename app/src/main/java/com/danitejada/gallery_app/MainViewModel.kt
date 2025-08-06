package com.danitejada.gallery_app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danitejada.core.domain.usecases.settings.GetApiKeyUseCase
import com.danitejada.core.domain.usecases.settings.SeedInitialApiKeyUseCase
import com.danitejada.gallery_app.navigation.ApiKeyDestination
import com.danitejada.gallery_app.navigation.PhotoListDestination
import com.danitejada.gallery_app.navigation.AppDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for managing the app's initial state and determining the start destination.
 *
 * It performs the following on initialization:
 * - Seeds the initial API key if needed.
 * - Resolves the start destination based on whether an API key exists.
 *
 * @property seedInitialApiKeyUseCase Use case for seeding a default or previously stored API key.
 * @property getApiKeyUseCase Use case for retrieving the current API key.
 */
@HiltViewModel
class MainViewModel @Inject constructor(
  private val seedInitialApiKeyUseCase: SeedInitialApiKeyUseCase,
  private val getApiKeyUseCase: GetApiKeyUseCase
) : ViewModel() {

  private val _uiState = MutableStateFlow<MainUiState>(MainUiState.Loading)
  val uiState = _uiState.asStateFlow()

  init {
    initializeApp()
  }

  /**
   * Initializes the app by checking for a saved API key.
   * Determines whether the user should navigate to the API key screen or the main screen.
   */
  private fun initializeApp() {
    viewModelScope.launch {
      seedInitialApiKeyUseCase()
      _uiState.value = MainUiState.Ready(resolveStartDestination())
    }
  }

  /**
   * Resolves which screen should be the start destination depending on whether an API key exists.
   *
   * @return [PhotoListDestination] if an API key exists; [ApiKeyDestination] otherwise.
   */
  private suspend fun resolveStartDestination(): AppDestination {
    return if (getApiKeyUseCase()?.value?.isNotBlank() == true) PhotoListDestination else ApiKeyDestination
  }
}