package com.danitejada.core.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos")
data class PhotoEntity(
  @PrimaryKey val id: Int,
  val width: Int?,
  val height: Int?,
  val url: String?,
  val photographer: String?,
  val photographerUrl: String?,
  val photographerId: Long?,
  val avgColor: String?,
  val thumbnailUrl: String?,
  val tinyThumbnailUrl: String?,
  val largeImageUrl: String?,
  val alt: String?
)