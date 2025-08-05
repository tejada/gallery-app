package com.danitejada.gallery_app

import app.cash.turbine.test
import com.danitejada.core.domain.usecases.settings.HasApiKeyUseCase
import com.danitejada.core.domain.usecases.settings.SeedInitialApiKeyUseCase
import com.danitejada.gallery_app.navigation.ApiKeyDestination
import com.danitejada.gallery_app.navigation.PhotoListDestination
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
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class MainViewModelTest {

  private lateinit var viewModel: MainViewModel
  private val seedInitialApiKeyUseCase: SeedInitialApiKeyUseCase = mock()
  private val hasApiKeyUseCase: HasApiKeyUseCase = mock()
  private val testDispatcher = StandardTestDispatcher()

  @Before
  fun setUp() {
    Dispatchers.setMain(testDispatcher)
  }

  @After
  fun tearDown() {
    Dispatchers.resetMain()
  }

  @Test
  fun `when api key exists, uiState is Ready with PhotoListDestination`() = runTest {
    whenever(hasApiKeyUseCase.invoke()).thenReturn(true)
    viewModel = MainViewModel(seedInitialApiKeyUseCase, hasApiKeyUseCase)

    viewModel.uiState.test {
      val readyState = awaitItem()
      assertTrue(readyState is MainUiState.Ready)
      assertEquals(PhotoListDestination, (readyState as MainUiState.Ready).startDestination)
    }
  }

  @Test
  fun `when api key does not exist, uiState is Ready with ApiKeyDestination`() = runTest {
    whenever(hasApiKeyUseCase.invoke()).thenReturn(false)
    viewModel = MainViewModel(seedInitialApiKeyUseCase, hasApiKeyUseCase)

    viewModel.uiState.test {
      val readyState = awaitItem()
      assertTrue(readyState is MainUiState.Ready)
      assertEquals(ApiKeyDestination, (readyState as MainUiState.Ready).startDestination)
    }
  }
}