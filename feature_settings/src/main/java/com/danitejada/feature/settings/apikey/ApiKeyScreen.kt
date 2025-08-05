package com.danitejada.feature.settings.apikey

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.danitejada.feature.settings.R

@Composable
fun ApiKeyScreen(
  viewModel: ApiKeyViewModel,
  onApiKeySaved: () -> Unit
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  var apiKeyInput by remember { mutableStateOf("") }

  LaunchedEffect(uiState) {
    if (uiState is ApiKeyUiState.Success && (uiState as ApiKeyUiState.Success).apiKey.isNotBlank()) {
      onApiKeySaved()
      // Optionally reset to Idle to prevent re-triggering
      viewModel.resetUiState()
    }
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Text(
      text = stringResource(R.string.api_key_screen_title),
      style = MaterialTheme.typography.titleLarge,
      modifier = Modifier.padding(bottom = 32.dp)
    )

    OutlinedTextField(
      value = apiKeyInput,
      onValueChange = { apiKeyInput = it },
      label = { Text(stringResource(R.string.api_key_label)) },
      // Remove PasswordVisualTransformation for better UX
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 16.dp),
      isError = uiState is ApiKeyUiState.Error,
      supportingText = {
        if (uiState is ApiKeyUiState.Error) {
          Text(
            text = stringResource((uiState as ApiKeyUiState.Error).messageResId),
            color = MaterialTheme.colorScheme.error
          )
        }
      }
    )

    Button(
      onClick = { viewModel.saveApiKey(apiKeyInput) },
      enabled = apiKeyInput.isNotBlank() && (uiState is ApiKeyUiState.Idle || uiState is ApiKeyUiState.Error),
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