package com.danitejada.core.domain.models

import androidx.compose.ui.graphics.Color

data class Photo(
  val id: Int,
  val type: String?,
  val width: Int?,
  val height: Int?,
  val url: String?,
  val photographer: String?,
  val photographerUrl: String?,
  val photographerId: Long?,
  val avgColor: Color?,
  val thumbnailUrl: String?,
  val tinyThumbnailUrl: String?,
  val largeImageUrl: String?,
  val liked: Boolean?,
  val alt: String?
)