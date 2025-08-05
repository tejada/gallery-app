package com.danitejada.feature.settings.apikey

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danitejada.core.domain.usecases.settings.SaveApiKeyUseCase
import com.danitejada.feature.settings.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ApiKeyViewModel @Inject constructor(
  private val useCase: SaveApiKeyUseCase,
) : ViewModel() {

  private var saveJob: Job? = null

  private val _uiState = MutableStateFlow<ApiKeyUiState>(ApiKeyUiState.Idle)
  val uiState: StateFlow<ApiKeyUiState> = _uiState.asStateFlow()

  fun saveApiKey(apiKey: String) {
    when {
      apiKey.isBlank() -> {
        _uiState.value = ApiKeyUiState.Error(R.string.error_api_key_empty)
        return
      }
      !isValidApiKeyFormat(apiKey) -> {
        _uiState.value = ApiKeyUiState.Error(R.string.error_api_key_invalid)
        return
      }
    }

    saveJob?.cancel()
    saveJob = viewModelScope.launch {
      _uiState.value = ApiKeyUiState.Loading
      try {
        useCase(apiKey)
        _uiState.value = ApiKeyUiState.Success(apiKey)
      } catch (e: Exception) {
        _uiState.value = ApiKeyUiState.Error(mapErrorToMessage(e))
      }
    }
  }

  private fun isValidApiKeyFormat(apiKey: String): Boolean {
    // Pexels API keys are typically 30-80 alphanumeric characters
    return apiKey.length in 30..80 && apiKey.matches(Regex("^[a-zA-Z0-9]+$"))
  }

  private fun mapErrorToMessage(e: Exception): Int = when (e) {
    is SecurityException -> R.string.error_security_generic
    is IllegalArgumentException -> R.string.error_api_key_invalid
    is IOException -> R.string.error_network
    else -> when {
      e.message?.contains("encryption", ignoreCase = true) == true ->
        R.string.error_encryption_failed
      e.message?.contains("keystore", ignoreCase = true) == true ->
        R.string.error_keystore_not_ready
      else -> R.string.error_save_api_key_failed_generic
    }
  }

  fun resetUiState() {
    _uiState.value = ApiKeyUiState.Idle
  }

  override fun onCleared() {
    super.onCleared()
    saveJob?.cancel()
  }
}