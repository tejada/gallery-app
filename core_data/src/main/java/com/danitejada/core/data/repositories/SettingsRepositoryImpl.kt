package com.danitejada.core.data.repositories

import com.danitejada.core.BuildConfig
import com.danitejada.core.data.local.preferences.SecurePreferencesDataSource
import com.danitejada.core.domain.models.ApiKey
import com.danitejada.core.domain.repositories.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepositoryImpl @Inject constructor(
  private val securePreferencesDataSource: SecurePreferencesDataSource
) : SettingsRepository {

  override suspend fun seedInitialApiKeyIfNeeded() {
    val isSeedComplete = securePreferencesDataSource.isInitialSeedComplete().first()
    if (!isSeedComplete) {
      val apiKeyFromProperties = BuildConfig.API_KEY
      if (apiKeyFromProperties.isNotBlank()) {
        saveApiKey(apiKeyFromProperties)
      }
      // Mark as complete even if the key was blank, to prevent re-running.
      securePreferencesDataSource.setInitialSeedComplete()
    }
  }

  override suspend fun saveApiKey(apiKey: String) {
    if (apiKey.isBlank()) {
      throw IllegalArgumentException("API key cannot be blank")
    }

    try {
      securePreferencesDataSource.saveApiKey(apiKey)
    } catch (e: SecurityException) {
      throw e
    } catch (e: Exception) {
      // Wrap other exceptions
      throw RuntimeException("Failed to save API key", e)
    }
  }

  override suspend fun getApiKey(): ApiKey? {
    return try {
      val apiKeyValue = securePreferencesDataSource.getApiKey().first()
      apiKeyValue?.let {
        ApiKey(
          value = it,
          isValid = it.isNotBlank()
        )
      }
    } catch (e: Exception) {
      // If we can't decrypt or retrieve the key, return null
      null
    }
  }

  override fun observeApiKey(): Flow<ApiKey?> {
    return securePreferencesDataSource.getApiKey().map { apiKey ->
      apiKey?.let {
        ApiKey(
          value = it,
          isValid = it.isNotBlank()
        )
      }
    }
  }

  override suspend fun hasValidApiKey(): Boolean {
    return try {
      val apiKey = getApiKey()
      apiKey?.value?.isNotBlank() == true
    } catch (e: Exception) {
      false
    }
  }

  override fun isInitialSeedComplete(): Flow<Boolean> {
    return securePreferencesDataSource.isInitialSeedComplete()
  }

  override suspend fun setInitialSeedComplete() {
    securePreferencesDataSource.setInitialSeedComplete()
  }

  suspend fun clearApiKey() {
    securePreferencesDataSource.clearApiKey()
  }
}