package com.danitejada.feature.photos.photos.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.unit.dp
import com.danitejada.core.domain.models.Photo
import com.danitejada.core.ui.image.AsyncImageWithPlaceholder

@Composable
fun PhotoItem(
  photo: Photo,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val placeholderColor = photo.avgColor ?: Color.LightGray
  Card(
    modifier = modifier
      .aspectRatio(1f)
      .clickable { onClick() },
    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
  ) {
    AsyncImageWithPlaceholder(
      imageUrl = photo.tinyThumbnailUrl,
      placeholder = ColorPainter(color = placeholderColor),
      contentDescription = photo.alt,
      modifier = Modifier
        .fillMaxSize()
        .clip(RoundedCornerShape(8.dp))
    )
  }
}