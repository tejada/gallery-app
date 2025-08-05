package com.danitejada.core.domain.usecases.settings

import com.danitejada.core.domain.repositories.SettingsRepository
import javax.inject.Inject

class HasApiKeyUseCase @Inject constructor(
  private val settingsRepository: SettingsRepository
) {
  suspend operator fun invoke(): Boolean {
    // Check if the API key is valid.
    return settingsRepository.hasValidApiKey()
  }
}