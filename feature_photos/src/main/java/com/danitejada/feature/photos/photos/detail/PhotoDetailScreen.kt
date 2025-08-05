package com.danitejada.feature.photos.photos.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.rememberAsyncImagePainter
import com.danitejada.core.ui.image.AsyncImageWithPlaceholder
import com.danitejada.feature.photos.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoDetailScreen(
  photoId: Int,
  viewModel: PhotoDetailViewModel,
  onBackClick: () -> Unit
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val uriHandler = LocalUriHandler.current

  LaunchedEffect(photoId) {
    viewModel.loadPhoto(photoId)
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text(stringResource(id = R.string.photo_details_title)) },
        navigationIcon = {
          IconButton(onClick = onBackClick) {
            Icon(
              Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back"
            )
          }
        }
      )
    }
  ) { paddingValues ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
    ) {
      when (uiState) {
        is PhotoDetailUiState.Loading -> {
          CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center)
          )
        }

        is PhotoDetailUiState.Error -> {
          Text(
            text = (uiState as PhotoDetailUiState.Error).message ?: "",
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier
              .align(Alignment.Center)
              .padding(16.dp)
          )
        }

        is PhotoDetailUiState.Success -> {
          val photo = (uiState as PhotoDetailUiState.Success).photo
          Column(
            modifier = Modifier
              .fillMaxSize()
              .verticalScroll(rememberScrollState())
          ) {
            // Calculate aspect ratio
            val aspectRatio = if (photo?.width != null &&
              photo.height != null &&
              photo.height != 0
            ) {
              photo.width!!.toFloat() / photo.height!!.toFloat()
            } else {
              1f
            }

            Box(
              modifier = Modifier.background(
                photo?.avgColor ?: Color.White
              )
            ) {
              AsyncImageWithPlaceholder(
                imageUrl = photo?.largeImageUrl,
                placeholder = rememberAsyncImagePainter(
                  model = photo?.tinyThumbnailUrl,
                  contentScale = ContentScale.FillBounds
                ),
                contentDescription = photo?.alt,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                  .fillMaxWidth()
                  .aspectRatio(aspectRatio)
              )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
              modifier = Modifier.padding(horizontal = 16.dp)
            ) {
              photo?.photographer?.let { photographer ->
                Text(
                  text = "Photographer: $photographer",
                  style = MaterialTheme.typography.titleMedium,
                  modifier = Modifier.padding(bottom = 8.dp)
                )
              }

              photo?.alt?.let { alt ->
                Text(
                  text = "Description: $alt",
                  style = MaterialTheme.typography.bodyMedium,
                  modifier = Modifier.padding(bottom = 8.dp)
                )
              }

              if (photo?.width != null && photo.height != null) {
                Text(
                  text = "Dimensions: ${photo.width}x${photo.height}",
                  style = MaterialTheme.typography.bodyMedium,
                  modifier = Modifier.padding(bottom = 8.dp)
                )
              }

              photo?.url?.let { url ->
                Text(
                  text = "View on the web",
                  style = MaterialTheme.typography.bodyMedium,
                  color = MaterialTheme.colorScheme.primary,
                  modifier = Modifier
                    .padding(bottom = 8.dp)
                    .clickable {
                      uriHandler.openUri(url)
                    }
                )
              }
            }
          }
        }
      }
    }
  }
}