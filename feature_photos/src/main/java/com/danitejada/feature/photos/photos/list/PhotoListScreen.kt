package com.danitejada.feature.photos.photos.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.danitejada.feature.photos.R
import com.danitejada.feature.photos.photos.components.PhotoItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoListScreen(
  viewModel: PhotoListViewModel,
  onPhotoClick: (Int) -> Unit,
  onSettingsClick: () -> Unit
) {
  val lazyPagingItems = viewModel.photos.collectAsLazyPagingItems()

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text(stringResource(id = R.string.photo_list_title)) },
        actions = {
          IconButton(onClick = onSettingsClick) {
            Icon(Icons.Default.Settings, contentDescription = stringResource(R.string.settings_button))
          }
        }
      )
    }
  ) { paddingValues ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues),
      contentAlignment = Alignment.Center
    ) {
      when (lazyPagingItems.loadState.refresh) {
        is LoadState.Loading -> {
          CircularProgressIndicator()
        }

        is LoadState.Error -> {
          val error = (lazyPagingItems.loadState.refresh as LoadState.Error).error
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = error.message ?: "An error occurred")
            Button(onClick = { lazyPagingItems.retry() }) {
              Text("Retry")
            }
          }
        }

        else -> {
          LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 120.dp),
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            items(lazyPagingItems.itemCount) { index ->
              val photo = lazyPagingItems[index]
              if (photo != null) {
                PhotoItem(photo = photo, onClick = { onPhotoClick(photo.id) })
              }
            }

            when (lazyPagingItems.loadState.append) {
              is LoadState.Loading -> {
                item {
                  Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                  ) {
                    CircularProgressIndicator()
                  }
                }
              }

              is LoadState.Error -> {
                val error = (lazyPagingItems.loadState.append as LoadState.Error).error
                item {
                  Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = error.message ?: stringResource(R.string.error_occurred))
                    Button(onClick = { lazyPagingItems.retry() }) {
                      Text(stringResource(R.string.retry_button))
                    }
                  }
                }
              }

              else -> {
                // Do nothing
              }
            }

            if (lazyPagingItems.itemCount == 0 && lazyPagingItems.loadState.refresh is LoadState.NotLoading) {
              item {
                Text(stringResource(R.string.no_photos_found))
              }
            }
          }
        }
      }
    }
  }
}