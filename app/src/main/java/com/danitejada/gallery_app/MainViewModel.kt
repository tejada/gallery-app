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
    initializeApp()
  }

  private fun initializeApp() {
    viewModelScope.launch {
      seedInitialApiKeyUseCase()
      _uiState.value = MainUiState.Ready(resolveStartDestination())
    }
  }

  private suspend fun resolveStartDestination(): Any {
    return if (hasApiKeyUseCase()) PhotoListDestination else ApiKeyDestination
  }
}