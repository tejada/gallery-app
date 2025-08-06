package com.danitejada.core.domain.usecases.settings

import com.danitejada.core.domain.models.ApiKey
import com.danitejada.core.domain.repositories.SettingsRepository
import javax.inject.Inject

class GetApiKeyUseCase @Inject constructor(
  private val settingsRepository: SettingsRepository
) {
  suspend operator fun invoke(): ApiKey? {
    // Get the API key
    return settingsRepository.getApiKey()
  }
}