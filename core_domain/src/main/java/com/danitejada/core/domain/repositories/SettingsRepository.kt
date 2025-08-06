package com.danitejada.core.domain.repositories

import com.danitejada.core.domain.models.ApiKey
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
  suspend fun seedInitialApiKeyIfNeeded()
  suspend fun saveApiKey(apiKey: String)
  suspend fun getApiKey(): ApiKey?
  fun isInitialSeedComplete(): Flow<Boolean>
  suspend fun setInitialSeedComplete()
}