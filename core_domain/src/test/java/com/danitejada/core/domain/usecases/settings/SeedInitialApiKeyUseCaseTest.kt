package com.danitejada.core.domain.usecases.settings

import com.danitejada.core.domain.repositories.SettingsRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class SeedInitialApiKeyUseCaseTest {

  private val settingsRepository: SettingsRepository = mock()

  @Test
  fun `invoke does not save key when seed is already complete`() = runBlocking {
    // Given the seed process is already complete
    whenever(settingsRepository.isInitialSeedComplete()).thenReturn(flowOf(true))
    val useCase = SeedInitialApiKeyUseCase(settingsRepository)

    // When the use case is invoked
    useCase()

    // Then the API key is not saved and the seed complete flag is not set again
    verify(settingsRepository, never()).saveApiKey(any())
    verify(settingsRepository, never()).setInitialSeedComplete()
  }

  @Test
  fun `invoke saves key when seed is not complete and key exists`() = runBlocking {
    // Given the seed process is not complete and there is an API key in BuildConfig
    whenever(settingsRepository.isInitialSeedComplete()).thenReturn(flowOf(false))
    val useCase = SeedInitialApiKeyUseCase(settingsRepository)

    // When the use case is invoked
    useCase()

    // Then the repository is called to seed the key and mark the process as complete
    verify(settingsRepository).seedInitialApiKeyIfNeeded()
  }
}