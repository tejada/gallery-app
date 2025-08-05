package com.danitejada.core.domain.usecases.settings

import com.danitejada.core.domain.repositories.SettingsRepository
import javax.inject.Inject

class SeedInitialApiKeyUseCase @Inject constructor(
  private val settingsRepository: SettingsRepository
) {
  suspend operator fun invoke() {
    settingsRepository.seedInitialApiKeyIfNeeded()
  }
}