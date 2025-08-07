package com.danitejada.core.domain.usecases.settings

import com.danitejada.core.domain.repositories.SettingsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for [SaveApiKeyUseCase].
 *
 * Verifies that the use case correctly delegates the saving of an API key
 * to the [SettingsRepository].
 */
class SaveApiKeyUseCaseTest {

  private lateinit var useCase: SaveApiKeyUseCase
  private val settingsRepository: SettingsRepository = mockk()

  // Test data
  private val testApiKey = "some_test_api_key_123"

  @Before
  fun setup() {
    useCase = SaveApiKeyUseCase(settingsRepository)
  }

  /**
   * Verifies that [invoke] calls the [SettingsRepository.saveApiKey] method
   * with the provided API key.
   */
  @Test
  fun `invoke calls repository's saveApiKey with correct value`() = runTest {
    // Given the repository is set up to successfully save the key
    coEvery { settingsRepository.saveApiKey(testApiKey) } returns Unit

    // When the use case is invoked to save an API key
    useCase(testApiKey)

    // Then the repository's saveApiKey method should be called exactly once with the given key
    coVerify(exactly = 1) { settingsRepository.saveApiKey(testApiKey) }
  }

  /**
   * Verifies that exceptions from the repository are propagated when calling [saveApiKey].
   */
  @Test(expected = SecurityException::class)
  fun `invoke propagates SecurityException from repository`() = runTest {
    // Given the repository throws a SecurityException when saving the key
    coEvery { settingsRepository.saveApiKey(any()) } throws SecurityException("Encryption failed")

    // When the use case is invoked
    useCase(testApiKey)

    // Then the exception should be propagated
  }

  /**
   * Verifies that exceptions from the repository are propagated when calling [saveApiKey].
   */
  @Test(expected = java.io.IOException::class)
  fun `invoke propagates IOException from repository`() = runTest {
    // Given the repository throws an IOException when saving the key
    coEvery { settingsRepository.saveApiKey(any()) } throws java.io.IOException("Storage error")

    // When the use case is invoked
    useCase(testApiKey)

    // Then the exception should be propagated
  }
}