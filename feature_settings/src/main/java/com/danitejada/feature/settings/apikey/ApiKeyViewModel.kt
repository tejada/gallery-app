package com.danitejada.feature.settings.apikey

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danitejada.common.R
import com.danitejada.core.domain.usecases.settings.GetApiKeyUseCase
import com.danitejada.core.domain.usecases.settings.SaveApiKeyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

/**
 * ViewModel for managing API key input and saving logic.
 *
 * @param saveApiKeyUseCase Use case for saving the API key.
 * @param getApiKeyUseCase Use case for retrieving the saved API key.
 */
@HiltViewModel
class ApiKeyViewModel @Inject constructor(
  private val saveApiKeyUseCase: SaveApiKeyUseCase,
  private val getApiKeyUseCase: GetApiKeyUseCase,
) : ViewModel() {

  private var saveJob: Job? = null

  private val _uiState = MutableStateFlow<ApiKeyUiState>(ApiKeyUiState.Idle(""))
  val uiState: StateFlow<ApiKeyUiState> = _uiState.asStateFlow()

  /**
   * Loads the saved API key from storage and updates the UI state.
   */
  fun loadApiKey() {
    viewModelScope.launch {
      val apiKey = getApiKeyUseCase()
      _uiState.value = ApiKeyUiState.Idle(apiKey?.value ?: "")
    }
  }

  /**
   * Saves the provided API key after validation. Launches a coroutine in [viewModelScope] to
   * save the API key securely.
   *
   * @param apiKey The API key to save.
   */
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
        saveApiKeyUseCase(apiKey)
        _uiState.value = ApiKeyUiState.Success(apiKey)
      } catch (e: Exception) {
        _uiState.value = ApiKeyUiState.Error(mapErrorToMessage(e))
      }
    }
  }
  /**
   * Validates the format of the API key.
   *
   * @param apiKey The API key to validate.
   * @return True if the API key is valid (30-80 alphanumeric characters), false otherwise.
   */
  private fun isValidApiKeyFormat(apiKey: String): Boolean {
    return apiKey.length in 30..80 && apiKey.matches(Regex("^[a-zA-Z0-9]+$"))
  }

  /**
   * Maps an exception to a string resource ID for error display.
   *
   * @param e The exception to map.
   * @return The resource ID of the error message.
   */
  private fun mapErrorToMessage(e: Exception): Int = when (e) {
    is SecurityException -> R.string.error_security_generic
    is IllegalArgumentException -> R.string.error_api_key_invalid
    is IOException -> R.string.error_network
    else -> when {
      e.message?.contains("encryption", ignoreCase = true) == true ->
        R.string.error_security_encryption_failed

      e.message?.contains("keystore", ignoreCase = true) == true ->
        R.string.error_security_keystore_not_ready

      else -> R.string.error_api_key_save_failed
    }
  }

  /**
   * Resets the UI state to the initial state by reloading the saved API key.
   */
  fun resetUiState() {
    loadApiKey()
  }

  override fun onCleared() {
    super.onCleared()
    saveJob?.cancel()
  }
}