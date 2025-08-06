package com.danitejada.feature.settings.apikey

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.danitejada.common.R

/**
 * Composable function that renders the API key input screen.
 *
 * @param viewModel The ViewModel for managing API key logic.
 * @param onApiKeySaved Callback invoked when the API key is successfully saved.
 * @param onBackClick Callback invoked when the back button is clicked.
 * @param navController The navigation controller for handling navigation.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApiKeyScreen(
  viewModel: ApiKeyViewModel,
  onApiKeySaved: () -> Unit,
  onBackClick: () -> Unit,
  navController: NavController
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val canNavigateBack = navController.previousBackStackEntry != null
  var apiKeyInput by remember { mutableStateOf("") }

  // Load the saved API key when the screen starts
  LaunchedEffect(Unit) {
    viewModel.loadApiKey()
  }

  // Pre-fill input field with saved key (once)
  LaunchedEffect(uiState) {
    if (uiState is ApiKeyUiState.Idle && apiKeyInput.isBlank()) {
      apiKeyInput = (uiState as ApiKeyUiState.Idle).apiKey
    }

    (uiState as? ApiKeyUiState.Success)?.let {
      if (it.apiKey.isNotBlank()) {
        onApiKeySaved()
        viewModel.resetUiState()
      }
    }
  }

  ApiKeyContent(
    uiState = uiState,
    apiKeyInput = apiKeyInput,
    onApiKeyInputChange = { apiKeyInput = it },
    onSaveClick = { viewModel.saveApiKey(apiKeyInput) },
    canNavigateBack = canNavigateBack,
    onBackClick = onBackClick
  )
}

/**
 * Renders the main content of the API key input screen, including the text field and save button.
 *
 * @param uiState The current UI state of the API key screen.
 * @param apiKeyInput The current value of the API key input field.
 * @param onApiKeyInputChange Callback invoked when the API key input changes.
 * @param onSaveClick Callback invoked when the save button is clicked.
 * @param canNavigateBack Whether the back button should be displayed.
 * @param onBackClick Callback invoked when the back button is clicked.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApiKeyContent(
  uiState: ApiKeyUiState,
  apiKeyInput: String,
  onApiKeyInputChange: (String) -> Unit,
  onSaveClick: () -> Unit,
  canNavigateBack: Boolean,
  onBackClick: () -> Unit
) {
  val focusManager = LocalFocusManager.current
  val keyboardController = LocalSoftwareKeyboardController.current

  Scaffold(
    topBar = {
      ApiKeyTopAppBar(
        canNavigateBack = canNavigateBack,
        onBackClick = onBackClick
      )
    }
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .padding(paddingValues)
        .fillMaxSize()
        .padding(16.dp)
        .verticalScroll(rememberScrollState())
        .imePadding(),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      Text(
        text = stringResource(R.string.settings_api_key_blurb),
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(bottom = 32.dp)
      )

      ApiKeyTextField(
        value = apiKeyInput,
        onValueChange = onApiKeyInputChange,
        uiState = uiState,
        onDone = {
          focusManager.clearFocus()
          keyboardController?.hide()
          onSaveClick()
        }
      )

      SaveButton(
        uiState = uiState,
        isInputValid = apiKeyInput.isNotBlank(),
        onClick = {
          focusManager.clearFocus()
          keyboardController?.hide()
          onSaveClick()
        }
      )
    }
  }
}

/**
 * Renders the top app bar for the API key input screen.
 *
 * @param canNavigateBack Whether the back button should be displayed.
 * @param onBackClick Callback invoked when the back button is clicked.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApiKeyTopAppBar(
  canNavigateBack: Boolean,
  onBackClick: () -> Unit
) {
  TopAppBar(
    title = {
      Text(
        text = stringResource(R.string.settings_screen_title),
        style = MaterialTheme.typography.headlineSmall
      )
    },
    navigationIcon = {
      if (canNavigateBack) {
        val backDescription = stringResource(R.string.content_description_navigate_back)
        IconButton(
          onClick = onBackClick,
          modifier = Modifier.semantics {
            contentDescription = backDescription
          }) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = null
          )
        }
      }
    }
  )
}

/**
 * Renders the text field for entering the API key.
 *
 * @param value The current value of the API key input.
 * @param onValueChange Triggers when the user updates the API key input.
 * @param uiState The current UI state of the API key screen.
 * @param onDone Callback invoked when the done action is triggered on the keyboard.
 */
@Composable
fun ApiKeyTextField(
  value: String,
  onValueChange: (String) -> Unit,
  uiState: ApiKeyUiState,
  onDone: () -> Unit
) {
  val isError = uiState is ApiKeyUiState.Error
  val isReadyToSubmit = uiState is ApiKeyUiState.Idle || isError

  OutlinedTextField(
    value = value,
    onValueChange = onValueChange,
    label = { Text(stringResource(R.string.settings_api_key_label)) },
    singleLine = true,
    keyboardOptions = KeyboardOptions(
      imeAction = ImeAction.Done,
      keyboardType = KeyboardType.Text
    ),
    keyboardActions = KeyboardActions(onDone = {
      if (value.isNotBlank() && isReadyToSubmit) {
        onDone()
      }
    }),
    isError = isError,
    supportingText = {
      if (uiState is ApiKeyUiState.Error) {
        Text(
          text = stringResource(uiState.messageResId),
          color = MaterialTheme.colorScheme.error
        )
      }
    },
    modifier = Modifier
      .fillMaxWidth()
      .padding(bottom = 16.dp)
  )
}

/**
 * Renders the save button for submitting the API key.
 *
 * @param uiState The current UI state of the API key screen.
 * @param isInputValid Indicates whether the input field has content and is ready to submit.
 * @param onClick Callback invoked when the save button is clicked.
 */
@Composable
fun SaveButton(
  uiState: ApiKeyUiState,
  isInputValid: Boolean,
  onClick: () -> Unit
) {
  val isReadyToSubmit = uiState is ApiKeyUiState.Idle || uiState is ApiKeyUiState.Error

  Button(
    onClick = onClick,
    enabled = isInputValid && isReadyToSubmit,
    modifier = Modifier.fillMaxWidth()
  ) {
    if (uiState is ApiKeyUiState.Loading) {
      CircularProgressIndicator(
        modifier = Modifier.size(24.dp),
        color = MaterialTheme.colorScheme.onPrimary,
        strokeWidth = 2.dp
      )
    } else {
      Text(stringResource(R.string.settings_button_save_key))
    }
  }
}