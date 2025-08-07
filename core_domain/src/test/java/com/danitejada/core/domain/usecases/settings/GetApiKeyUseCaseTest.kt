package com.danitejada.core.domain.usecases.settings

import com.danitejada.core.domain.models.ApiKey
import com.danitejada.core.domain.repositories.SettingsRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for [GetApiKeyUseCase].
 *
 * Verifies that the use case correctly retrieves the API key
 * from the [SettingsRepository].
 */
class GetApiKeyUseCaseTest {

  private lateinit var useCase: GetApiKeyUseCase
  private val settingsRepository: SettingsRepository = mockk()

  // Test data
  private val testApiKey = ApiKey("retrieved_api_key_abc")

  @Before
  fun setup() {
    useCase = GetApiKeyUseCase(settingsRepository)
  }

  /**
   * Verifies that [invoke] returns the API key when the [SettingsRepository] provides one.
   */
  @Test
  fun `invoke returns API key when repository provides one`() = runTest {
    // Given the repository is set up to return a specific API key
    coEvery { settingsRepository.getApiKey() } returns testApiKey

    // When the use case is invoked to get the API key
    val result = useCase()

    // Then the returned result should match the API key from the repository
    assertEquals(testApiKey, result)
  }

  /**
   * Verifies that [invoke] returns null when the [SettingsRepository] provides null.
   */
  @Test
  fun `invoke returns null when repository provides null`() = runTest {
    // Given the repository is set up to return null (no API key stored)
    coEvery { settingsRepository.getApiKey() } returns null

    // When the use case is invoked to get the API key
    val result = useCase()

    // Then the returned result should be null
    assertNull(result)
  }

  /**
   * Verifies that exceptions from the repository are propagated when calling [getApiKey].
   */
  @Test(expected = java.io.IOException::class)
  fun `invoke propagates IOException from repository`() = runTest {
    // Given the repository throws an IOException when retrieving the key
    coEvery { settingsRepository.getApiKey() } throws java.io.IOException("Storage read error")

    // When the use case is invoked
    useCase()

    // Then the exception should be propagated
  }
}