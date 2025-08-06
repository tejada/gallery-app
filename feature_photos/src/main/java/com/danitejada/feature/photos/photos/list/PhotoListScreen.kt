package com.danitejada.feature.photos.photos.list

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.danitejada.common.R
import com.danitejada.core.domain.models.Photo
import com.danitejada.feature.photos.photos.components.PhotoItem

private sealed class PagingState {
  object Loading : PagingState()
  data class Error(val throwable: Throwable) : PagingState()
  object Empty : PagingState()
  object Data : PagingState()
}

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
        title = {
          Text(
            text = stringResource(R.string.photo_list_screen_title),
            style = MaterialTheme.typography.headlineSmall
          )
        },
        actions = {
          val iconDescription = stringResource(R.string.content_description_open_settings)
          IconButton(
            onClick = onSettingsClick,
            modifier = Modifier.semantics {
              contentDescription = iconDescription
            }
          ) {
            Icon(
              imageVector = Icons.Default.Settings,
              contentDescription = null
            )
          }
        }
      )
    }
  ) { paddingValues ->
    PhotoListContent(
      lazyPagingItems = lazyPagingItems,
      onPhotoClick = onPhotoClick,
      modifier = Modifier.padding(paddingValues)
    )
  }
}

@Composable
private fun rememberPagingState(items: LazyPagingItems<*>): PagingState {
  val refreshState = items.loadState.refresh

  return when {
    refreshState is LoadState.Loading && items.itemCount == 0 -> PagingState.Loading
    refreshState is LoadState.Error -> PagingState.Error(refreshState.error)
    items.itemCount == 0 && refreshState is LoadState.NotLoading -> PagingState.Empty
    else -> PagingState.Data
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PhotoListContent(
  lazyPagingItems: LazyPagingItems<Photo>,
  onPhotoClick: (Int) -> Unit,
  modifier: Modifier = Modifier
) {
  val state = rememberPullToRefreshState()
  val pagingState = rememberPagingState(lazyPagingItems)
  val isRefreshing =
    lazyPagingItems.loadState.refresh is LoadState.Loading && lazyPagingItems.itemCount > 0

  PullToRefreshBox(
    isRefreshing = isRefreshing,
    onRefresh = { lazyPagingItems.refresh() },
    modifier = modifier.fillMaxSize(),
    state = state
  ) {
    PagingContent(
      pagingState = pagingState,
      lazyPagingItems = lazyPagingItems,
      onPhotoClick = onPhotoClick,
      modifier = Modifier.fillMaxSize()
    )
  }
}

@Composable
private fun PagingContent(
  pagingState: PagingState,
  lazyPagingItems: LazyPagingItems<Photo>,
  onPhotoClick: (Int) -> Unit,
  modifier: Modifier = Modifier
) {
  when (pagingState) {
    is PagingState.Loading -> LoadingState(modifier)
    is PagingState.Error -> ErrorState(
      error = pagingState.throwable,
      onRetry = { lazyPagingItems.retry() },
      modifier = modifier
    )

    is PagingState.Empty -> EmptyState(modifier)
    is PagingState.Data -> PhotoGrid(
      lazyPagingItems = lazyPagingItems,
      onPhotoClick = onPhotoClick,
      modifier = modifier
    )
  }
}

@Composable
private fun LoadingState(
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier.fillMaxSize(),
    contentAlignment = Alignment.Center
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      CircularProgressIndicator(
        modifier = Modifier.size(48.dp),
        color = MaterialTheme.colorScheme.primary
      )
      Text(
        text = stringResource(R.string.photo_list_loading_message),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
      )
    }
  }
}

@Composable
private fun ErrorState(
  error: Throwable,
  onRetry: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(24.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Text(
      text = stringResource(R.string.error_photo_list_load_failed),
      style = MaterialTheme.typography.headlineSmall,
      textAlign = TextAlign.Center,
      color = MaterialTheme.colorScheme.error
    )

    Text(
      text = error.message ?: stringResource(R.string.error_generic),
      style = MaterialTheme.typography.bodyMedium,
      textAlign = TextAlign.Center,
      modifier = Modifier.padding(vertical = 16.dp),
      color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
    )

    Button(
      onClick = onRetry,
      modifier = Modifier.padding(top = 8.dp)
    ) {
      Text(stringResource(R.string.common_button_retry))
    }
  }
}

@Composable
private fun PhotoGrid(
  lazyPagingItems: LazyPagingItems<Photo>,
  onPhotoClick: (Int) -> Unit,
  modifier: Modifier = Modifier
) {
  val gridState = rememberLazyGridState()

  LazyVerticalGrid(
    columns = GridCells.Adaptive(minSize = 120.dp),
    state = gridState,
    modifier = modifier
      .fillMaxSize()
      .padding(8.dp),
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    // Actual photo items
    items(
      count = lazyPagingItems.itemCount,
      key = { index -> lazyPagingItems.peek(index)?.id ?: index }
    ) { index ->
      val photo = lazyPagingItems[index]

      if (photo != null) {
        PhotoItem(
          photo = photo,
          onClick = { onPhotoClick(photo.id) },
          modifier = Modifier.aspectRatio(1f)
        )
      } else {
        // Show shimmer for loading items
        ShimmerPhotoItem(
          modifier = Modifier.aspectRatio(1f)
        )
      }
    }

    // Handle empty state
    if (lazyPagingItems.itemCount == 0 &&
      lazyPagingItems.loadState.refresh is LoadState.NotLoading
    ) {
      item(span = { GridItemSpan(maxLineSpan) }) {
        EmptyState(
          modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp)
        )
      }
    }

    // Handle append loading states
    when (val appendState = lazyPagingItems.loadState.append) {
      is LoadState.Loading -> {
        item(span = { GridItemSpan(this.maxLineSpan) }) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp),
            contentAlignment = Alignment.Center
          ) {
            CircularProgressIndicator(
              modifier = Modifier.size(32.dp),
              strokeWidth = 3.dp
            )
          }
        }
      }

      is LoadState.Error -> {
        item(span = { GridItemSpan(this.maxLineSpan) }) {
          AppendErrorItem(
            error = appendState.error,
            onRetry = { lazyPagingItems.retry() }
          )
        }
      }

      else -> { /* No additional content needed */
      }
    }
  }
}

@Composable
private fun EmptyState(
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Text(
      text = stringResource(R.string.photo_list_empty_state_title),
      style = MaterialTheme.typography.headlineSmall,
      textAlign = TextAlign.Center,
      color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
    )

    Text(
      text = stringResource(R.string.photo_list_empty_state_description),
      style = MaterialTheme.typography.bodyMedium,
      textAlign = TextAlign.Center,
      modifier = Modifier.padding(top = 8.dp),
      color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
    )
  }
}

@Composable
private fun AppendErrorItem(
  error: Throwable,
  onRetry: () -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text(
      text = error.message ?: stringResource(R.string.error_photo_list_append_failed),
      style = MaterialTheme.typography.bodyMedium,
      textAlign = TextAlign.Center,
      color = MaterialTheme.colorScheme.error
    )

    Button(
      onClick = onRetry,
      modifier = Modifier.padding(top = 8.dp)
    ) {
      Text(stringResource(R.string.common_button_retry))
    }
  }
}

@Composable
private fun ShimmerPhotoItem(
  modifier: Modifier = Modifier
) {
  val shimmerSize = 120.dp
  Card(
    modifier = modifier
      .size(shimmerSize)
      .shimmerEffect(shimmerSize)
  ) {
    Box(modifier = Modifier.fillMaxSize())
  }
}

private fun Modifier.shimmerEffect(size: Dp): Modifier = composed {
  val sizePx = with(LocalDensity.current) { size.toPx() }
  val transition = rememberInfiniteTransition(label = "shimmer")
  val startOffsetX by transition.animateFloat(
    initialValue = -2 * sizePx,
    targetValue = 2 * sizePx,
    animationSpec = infiniteRepeatable(tween(1000)),
    label = "shimmer_offset"
  )

  background(
    brush = Brush.linearGradient(
      colors = listOf(
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
      ),
      start = Offset(startOffsetX, 0f),
      end = Offset(startOffsetX + sizePx, sizePx)
    )
  )
}