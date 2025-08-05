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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.danitejada.feature.settings.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApiKeyScreen(
  viewModel: ApiKeyViewModel,
  onApiKeySaved: () -> Unit,
  onBackClick: () -> Unit,
  navController: NavController
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  var apiKeyInput by remember { mutableStateOf("") }

  val scrollState = rememberScrollState()
  val focusManager = LocalFocusManager.current
  val keyboardController = LocalSoftwareKeyboardController.current

  val isInputValid = apiKeyInput.isNotBlank()
  val isReadyToSubmit = uiState is ApiKeyUiState.Idle || uiState is ApiKeyUiState.Error
  val canNavigateBack = navController.previousBackStackEntry != null

  // React to a successful save
  LaunchedEffect(uiState) {
    (uiState as? ApiKeyUiState.Success)?.let {
      if (it.apiKey.isNotBlank()) {
        onApiKeySaved()
        viewModel.resetUiState()
      }
    }
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text(stringResource(R.string.settings_title)) },
        navigationIcon = {
          if (canNavigateBack) {
            IconButton(onClick = onBackClick) {
              Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
          }
        }
      )
    }
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .padding(paddingValues)
        .fillMaxSize()
        .padding(16.dp)
        .verticalScroll(scrollState)
        .imePadding(),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      Text(
        text = stringResource(R.string.api_key_screen_blurb),
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(bottom = 32.dp)
      )

      OutlinedTextField(
        value = apiKeyInput,
        onValueChange = { apiKeyInput = it },
        label = { Text(stringResource(R.string.api_key_label)) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
          imeAction = ImeAction.Done,
          keyboardType = KeyboardType.Text
        ),
        keyboardActions = KeyboardActions(
          onDone = {
            if (isInputValid && isReadyToSubmit) {
              viewModel.saveApiKey(apiKeyInput)
              focusManager.clearFocus()
              keyboardController?.hide()
            }
          }
        ),
        isError = uiState is ApiKeyUiState.Error,
        supportingText = {
          if (uiState is ApiKeyUiState.Error) {
            Text(
              text = stringResource((uiState as ApiKeyUiState.Error).messageResId),
              color = MaterialTheme.colorScheme.error
            )
          }
        },
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 16.dp)
      )

      Button(
        onClick = {
          focusManager.clearFocus()
          keyboardController?.hide()
          viewModel.saveApiKey(apiKeyInput)
        },
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
          Text(stringResource(R.string.save_key_button))
        }
      }
    }
  }
}