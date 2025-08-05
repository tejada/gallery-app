package com.danitejada.feature.settings.apikey

import app.cash.turbine.test
import com.danitejada.core.domain.usecases.settings.SaveApiKeyUseCase
import com.danitejada.feature.settings.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class ApiKeyViewModelTest {

  private lateinit var viewModel: ApiKeyViewModel
  private val saveApiKeyUseCase: SaveApiKeyUseCase = mock()
  private val testDispatcher = StandardTestDispatcher()

  @Before
  fun setUp() {
    Dispatchers.setMain(testDispatcher)
    viewModel = ApiKeyViewModel(saveApiKeyUseCase)
  }

  @After
  fun tearDown() {
    Dispatchers.resetMain()
  }

  @Test
  fun `saveApiKey with blank key should emit error`() = runTest {
    viewModel.uiState.test {
      // Initial Idle state
      assertEquals(ApiKeyUiState.Idle, awaitItem())

      viewModel.saveApiKey(" ")

      val errorState = awaitItem()
      assertTrue(errorState is ApiKeyUiState.Error)
      assertEquals(R.string.error_api_key_empty, (errorState as ApiKeyUiState.Error).messageResId)

      verify(saveApiKeyUseCase, never()).invoke(any())
    }
  }

  @Test
  fun `saveApiKey with invalid format should emit error`() = runTest {
    viewModel.uiState.test {
      assertEquals(ApiKeyUiState.Idle, awaitItem())

      viewModel.saveApiKey("short")

      val errorState = awaitItem()
      assertTrue(errorState is ApiKeyUiState.Error)
      assertEquals(R.string.error_api_key_invalid, (errorState as ApiKeyUiState.Error).messageResId)
      verify(saveApiKeyUseCase, never()).invoke(any())
    }
  }

  @Test
  fun `saveApiKey with valid key should emit loading then success`() = runTest {
    val apiKey = "563492ad6f91700001000001abc123def456"
    viewModel.uiState.test {
      assertEquals(ApiKeyUiState.Idle, awaitItem())

      viewModel.saveApiKey(apiKey)

      assertEquals(ApiKeyUiState.Loading, awaitItem())

      val successState = awaitItem()
      assertTrue(successState is ApiKeyUiState.Success)
      assertEquals(apiKey, (successState as ApiKeyUiState.Success).apiKey)

      verify(saveApiKeyUseCase).invoke(apiKey)
    }
  }

  @Test
  fun `saveApiKey use case throws SecurityException should emit error`() = runTest {
    val apiKey = "563492ad6f91700001000001abc123def456"
    whenever(saveApiKeyUseCase.invoke(apiKey)).thenThrow(SecurityException("Security error: Unable to securely store the API key."))

    viewModel.uiState.test {
      assertEquals(ApiKeyUiState.Idle, awaitItem())
      viewModel.saveApiKey(apiKey)

      assertEquals(ApiKeyUiState.Loading, awaitItem())

      val errorState = awaitItem()
      assertTrue(errorState is ApiKeyUiState.Error)
      assertEquals(R.string.error_security_generic, (errorState as ApiKeyUiState.Error).messageResId)
    }
  }

  @Test
  fun `resetUiState should set state to Idle`() = runTest {
    viewModel.uiState.test {
      assertEquals(ApiKeyUiState.Idle, awaitItem())
      viewModel.saveApiKey(" ") // make state Error
      assertTrue(awaitItem() is ApiKeyUiState.Error)

      viewModel.resetUiState()
      assertEquals(ApiKeyUiState.Idle, awaitItem())
    }
  }
}