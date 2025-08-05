package com.danitejada.core.domain.usecases.settings

import com.danitejada.core.domain.repositories.SettingsRepository
import javax.inject.Inject

class SaveApiKeyUseCase @Inject constructor(
  private val settingsRepository: SettingsRepository
) {
  suspend operator fun invoke(apiKey: String) {
    settingsRepository.saveApiKey(apiKey)
  }
}