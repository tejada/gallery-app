package com.danitejada.core.data.mappers

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import com.danitejada.core.data.local.entities.PhotoEntity
import com.danitejada.core.data.remote.dto.PhotoDto
import com.danitejada.core.data.remote.dto.PhotosResponseDto
import com.danitejada.core.domain.models.Photo
import com.danitejada.core.domain.models.Photos
import javax.inject.Inject

class PhotoMapper @Inject constructor() {

  // Main function to map the entire network response.
  fun mapDtoToDomain(dto: PhotosResponseDto): Photos {
    return Photos(
      photos = dto.photos.map { mapDtoToDomain(it) },
      totalResults = dto.totalResults,
      page = dto.page,
      perPage = dto.perPage,
      hasNextPage = !dto.nextPage.isNullOrEmpty()
    )
  }

  // Maps a single network DTO to the domain model.
  fun mapDtoToDomain(dto: PhotoDto): Photo {
    return Photo(
      id = dto.id,
      type = dto.type,
      width = dto.width,
      height = dto.height,
      url = dto.url,
      photographer = dto.photographer,
      photographerUrl = dto.photographerUrl,
      photographerId = dto.photographerId,
      avgColor = parseAvgColor(dto.avgColor), // Use the helper
      thumbnailUrl = dto.src?.medium,
      tinyThumbnailUrl = dto.src?.tiny,
      largeImageUrl = dto.src?.large,
      liked = dto.liked,
      alt = dto.alt
    )
  }

  // Maps a database entity to the domain model.
  fun mapEntityToDomain(entity: PhotoEntity): Photo {
    return Photo(
      id = entity.id,
      type = null, // 'type' is not stored in the database
      width = entity.width,
      height = entity.height,
      url = entity.url,
      photographer = entity.photographer,
      photographerUrl = entity.photographerUrl,
      photographerId = entity.photographerId,
      avgColor = parseAvgColor(entity.avgColor), // Use the same helper
      thumbnailUrl = entity.thumbnailUrl,
      tinyThumbnailUrl = entity.tinyThumbnailUrl,
      largeImageUrl = entity.largeImageUrl,
      liked = null, // 'liked' is not stored in the database
      alt = entity.alt
    )
  }

  // Maps a domain model to a database entity.
  fun mapDomainToEntity(domain: Photo): PhotoEntity {
    return PhotoEntity(
      id = domain.id,
      width = domain.width,
      height = domain.height,
      url = domain.url,
      photographer = domain.photographer,
      photographerUrl = domain.photographerUrl,
      photographerId = domain.photographerId,
      avgColor = domain.avgColor?.let { String.format("#%08X", it.value.toLong()) },
      thumbnailUrl = domain.thumbnailUrl,
      tinyThumbnailUrl = domain.tinyThumbnailUrl,
      largeImageUrl = domain.largeImageUrl,
      alt = domain.alt
    )
  }

  /**
   * Private helper function to parse a color string.
   * This is now the single source of truth for color conversion logic.
   */
  private fun parseAvgColor(colorString: String?): Color {
    val defaultColor = Color.LightGray
    return try {
      colorString?.toColorInt()?.let { Color(it) } ?: defaultColor
    } catch (_: IllegalArgumentException) {
      defaultColor
    }
  }
}