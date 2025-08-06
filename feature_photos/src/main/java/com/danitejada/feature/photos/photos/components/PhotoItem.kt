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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.danitejada.common.R
import com.danitejada.core.domain.models.Photo
import com.danitejada.core.ui.image.AsyncImageWithPlaceholder

@Composable
fun PhotoItem(
  photo: Photo,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val placeholderColor = photo.avgColor ?: Color.LightGray
  val description = photo.alt ?: stringResource(
    R.string.photo_list_item_attribution,
    photo.photographer ?: ""
  )
  val label = stringResource(R.string.content_description_photo_item, photo.photographer ?: "")
  Card(
    modifier = modifier
      .aspectRatio(1f)
      .clickable(
        onClickLabel = label
      ) { onClick() }
      .semantics {
        contentDescription = description
        role = Role.Image
      },
    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
  ) {
    AsyncImageWithPlaceholder(
      imageUrl = photo.tinyThumbnailUrl,
      placeholder = ColorPainter(placeholderColor),
      contentDescription = description,
      modifier = Modifier
        .fillMaxSize()
        .clip(RoundedCornerShape(8.dp))
    )
  }
}