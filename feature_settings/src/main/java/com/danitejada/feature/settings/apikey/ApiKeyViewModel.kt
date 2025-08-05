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

  /**
   * Saves the provided API key and updates the UI state.
   * @param apiKey The API key to save.
   */
  fun saveApiKey(apiKey: String) {
    if (apiKey.isBlank()) {
      _uiState.value = ApiKeyUiState.Error(R.string.error_api_key_empty)
      return
    }

    // Basic API key format validation (Pexels API keys are typically 563492ad6f91700001000001...)
    if (!isValidApiKeyFormat(apiKey)) {
      _uiState.value = ApiKeyUiState.Error(R.string.error_api_key_invalid)
      return
    }

    saveJob?.cancel()
    saveJob = viewModelScope.launch {
      _uiState.value = ApiKeyUiState.Loading
      try {
        useCase(apiKey)
        _uiState.value = ApiKeyUiState.Success(apiKey)
      } catch (_: SecurityException) {
        _uiState.value = ApiKeyUiState.Error(R.string.error_security_generic)
      } catch (_: IllegalArgumentException) {
        _uiState.value = ApiKeyUiState.Error(R.string.error_api_key_invalid)
      } catch (_: IOException) {
        _uiState.value = ApiKeyUiState.Error(R.string.error_network)
      } catch (e: Exception) {
        val messageResId = when {
          e.message?.contains("encryption", ignoreCase = true) == true ->
            R.string.error_encryption_failed

          e.message?.contains("keystore", ignoreCase = true) == true ->
            R.string.error_keystore_not_ready

          else -> R.string.error_save_api_key_failed_generic
        }
        _uiState.value = ApiKeyUiState.Error(messageResId)
      }
    }
  }

  /**
   * Basic validation for Pexels API key format
   */
  private fun isValidApiKeyFormat(apiKey: String): Boolean {
    // Pexels API keys are typically 39-40 characters long, alphanumeric
    return apiKey.length in 30..50 && apiKey.matches(Regex("^[a-zA-Z0-9]+$"))
  }

  /**
   * Resets the UI state to Idle.
   */
  fun resetUiState() {
    _uiState.value = ApiKeyUiState.Idle
  }

  override fun onCleared() {
    super.onCleared()
    saveJob?.cancel()
  }
}