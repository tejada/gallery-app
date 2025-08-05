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
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.rememberAsyncImagePainter
import com.danitejada.core.domain.models.Photo
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
        title = { Text(stringResource(R.string.photo_details_title)) },
        navigationIcon = {
          IconButton(onClick = onBackClick) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
          }
        }
      )
    }
  ) { paddingValues ->
    Box(
      modifier = Modifier
        .padding(paddingValues)
        .fillMaxSize()
    ) {
      when (val state = uiState) {
        is PhotoDetailUiState.Loading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
        is PhotoDetailUiState.Error -> Text(
          text = state.message ?: "Unknown Error",
          modifier = Modifier.align(Alignment.Center)
        )

        is PhotoDetailUiState.Success -> PhotoDetailContent(state.photo, uriHandler)
      }
    }
  }
}

@Composable
private fun PhotoDetailContent(photo: Photo?, uriHandler: UriHandler) {
  val aspectRatio = (photo?.width?.takeIf { it > 0 }?.toFloat()
    ?.div(photo.height ?: 1)) ?: 1f

  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
  ) {
    Box(modifier = Modifier.background(photo?.avgColor ?: Color.White)) {
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

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
      photo?.photographer?.takeIf { it.isNotBlank() }?.let {
        DetailLabel(title = stringResource(R.string.photo_details_photographer), value = it)
      }

      photo?.alt?.takeIf { it.isNotBlank() }?.let {
        DetailLabel(title = stringResource(R.string.photo_details_description), value = it)
      }

      if (photo?.width != null && photo.height != null) {
        DetailLabel(
          title = stringResource(R.string.photo_details_dimensions),
          value = "${photo.width}x${photo.height}"
        )
      }

      photo?.url?.let { url ->
        Text(
          text = stringResource(R.string.photo_details_view_on_the_web),
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.primary,
          modifier = Modifier
            .padding(bottom = 8.dp)
            .clickable { uriHandler.openUri(url) }
        )
      }

      Spacer(modifier = Modifier.height(16.dp))
    }
  }
}

@Composable
private fun DetailLabel(title: String, value: String) {
  Text(
    text = "$title: $value",
    style = MaterialTheme.typography.bodyMedium,
    modifier = Modifier.padding(bottom = 8.dp)
  )
}