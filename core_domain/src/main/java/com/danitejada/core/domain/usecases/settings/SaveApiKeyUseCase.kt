package com.danitejada.core.domain.usecases.settings

import com.danitejada.core.domain.repositories.SettingsRepository
import javax.inject.Inject

/**
 * Use case for saving an API key.
 */
class SaveApiKeyUseCase @Inject constructor(
  private val settingsRepository: SettingsRepository
) {

  /**
   * Saves the provided API key to the repository.
   *
   * @param apiKey The API key to save.
   * @throws SecurityException If there is an issue with encryption or secure storage.
   * @throws java.io.IOException If there is an issue accessing the storage.
   */
  suspend operator fun invoke(apiKey: String) {
    settingsRepository.saveApiKey(apiKey)
  }
}