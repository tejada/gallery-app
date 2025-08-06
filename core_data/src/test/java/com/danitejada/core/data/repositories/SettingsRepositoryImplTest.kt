package com.danitejada.core.data.repositories

import com.danitejada.core.data.local.preferences.SecurePreferencesDataSource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class SettingsRepositoryImplTest {

  private val securePreferencesDataSource: SecurePreferencesDataSource = mock()
  private val repository = SettingsRepositoryImpl(securePreferencesDataSource)

  @Test
  fun `saveApiKey calls data source`() = runBlocking {
    val apiKey = "test_key"
    repository.saveApiKey(apiKey)
    verify(securePreferencesDataSource).saveApiKey(apiKey)
  }

  @Test(expected = IllegalArgumentException::class)
  fun `saveApiKey with blank key throws exception`() = runBlocking {
    repository.saveApiKey(" ")
  }

  @Test
  fun `getApiKey returns key when it exists`() = runBlocking {
    val apiKey = "test_key"
    whenever(securePreferencesDataSource.getApiKey()).thenReturn(flowOf(apiKey))
    val result = repository.getApiKey()
    assertNotNull(result)
    assertEquals(apiKey, result?.value)
  }

  @Test
  fun `getApiKey returns null when it does not exist`() = runBlocking {
    whenever(securePreferencesDataSource.getApiKey()).thenReturn(flowOf(null))
    val result = repository.getApiKey()
    assertNull(result)
  }

  @Test
  fun `isInitialSeedComplete forwards the call`() = runBlocking {
    whenever(securePreferencesDataSource.isInitialSeedComplete()).thenReturn(flowOf(true))
    val result = repository.isInitialSeedComplete().first()
    assertTrue(result)
  }

  @Test
  fun `setInitialSeedComplete forwards the call`() = runBlocking {
    repository.setInitialSeedComplete()
    verify(securePreferencesDataSource).setInitialSeedComplete()
  }
}