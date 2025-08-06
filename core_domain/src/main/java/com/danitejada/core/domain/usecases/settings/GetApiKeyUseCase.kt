package com.danitejada.core.domain.usecases.settings

import com.danitejada.core.domain.models.ApiKey
import com.danitejada.core.domain.repositories.SettingsRepository
import javax.inject.Inject

/**
 * Use case for retrieving the stored API key.
 */
class GetApiKeyUseCase @Inject constructor(
  private val settingsRepository: SettingsRepository
) {

  /**
   * Retrieves the stored API key from the repository.
   *
   * @return The [ApiKey] if set, or null if not available.
   * @throws java.io.IOException If there is an issue accessing the storage.
   */
  suspend operator fun invoke(): ApiKey? {
    return settingsRepository.getApiKey()
  }
}