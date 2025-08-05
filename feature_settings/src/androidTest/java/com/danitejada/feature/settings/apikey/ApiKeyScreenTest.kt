package com.danitejada.feature.settings.apikey

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.danitejada.core.domain.usecases.settings.SaveApiKeyUseCase
import com.danitejada.core.ui.theme.GalleryAppTheme
import io.mockk.coVerify
import io.mockk.mockk

import org.junit.Rule
import org.junit.Test

class ApiKeyScreenTest {

  @get:Rule
  val composeTestRule = createComposeRule()

  private val saveApiKeyUseCase: SaveApiKeyUseCase = mockk(relaxed = true)

  @Test
  fun saveButton_isDisabled_whenInputIsEmpty() {
    // Given the screen is launched with an empty input
    val viewModel = ApiKeyViewModel(saveApiKeyUseCase)
    composeTestRule.setContent {
      GalleryAppTheme {
        ApiKeyScreen(
          viewModel = viewModel,
          onApiKeySaved = {}
        )
      }
    }

    // Then the "Save Key" button is disabled
    composeTestRule.onNodeWithText("Save Key").assertIsNotEnabled()
  }

  @Test
  fun saveButton_isEnabled_whenInputIsNotEmpty() {
    // Given the screen is launched
    val viewModel = ApiKeyViewModel(saveApiKeyUseCase)
    composeTestRule.setContent {
      GalleryAppTheme {
        ApiKeyScreen(
          viewModel = viewModel,
          onApiKeySaved = {}
        )
      }
    }

    // When text is entered into the text field
    composeTestRule.onNodeWithText("API Key").performTextInput("some-valid-key")

    // Then the "Save Key" button is enabled
    composeTestRule.onNodeWithText("Save Key").assertIsEnabled()
  }

  @Test
  fun clickingSaveButton_withValidInput_callsViewModel() {
    // Given the screen is launched
    val viewModel = ApiKeyViewModel(saveApiKeyUseCase)
    composeTestRule.setContent {
      GalleryAppTheme {
        ApiKeyScreen(viewModel = viewModel, onApiKeySaved = {})
      }
    }

    val apiKey = "563492ad6f91700001000001abc123def456"

    // When valid text is entered and the save button is clicked
    composeTestRule.onNodeWithText("API Key").performTextInput(apiKey)
    composeTestRule.onNodeWithText("Save Key").performClick()

    // Then the saveApiKey method on the use case is called
    coVerify { saveApiKeyUseCase.invoke(apiKey) }
  }
}