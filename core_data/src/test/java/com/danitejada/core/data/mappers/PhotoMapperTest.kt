package com.danitejada.core.data.mappers

import androidx.compose.ui.graphics.Color
import com.danitejada.core.data.local.entities.PhotoEntity
import com.danitejada.core.data.remote.dto.PhotoDto
import com.danitejada.core.data.remote.dto.PhotoSourceDto
import com.danitejada.core.domain.models.Photo
import org.junit.Assert
import org.junit.Test

class PhotoMapperTest {

  private val mapper = PhotoMapper()

  @Test
  fun `mapDtoToDomain maps correctly`() {
    val dto = PhotoDto(
      id = 1,
      width = 100,
      height = 200,
      url = "url",
      photographer = "Dani",
      photographerUrl = "photographerUrl",
      photographerId = 1L,
      avgColor = "#FF0000",
      src = PhotoSourceDto(medium = "medium", tiny = "tiny", large = "large"),
      liked = true,
      alt = "alt"
    )

    val domain = mapper.mapDtoToDomain(dto)

    Assert.assertEquals(1, domain.id)
    Assert.assertEquals(Color(0xFFFF0000), domain.avgColor)
    Assert.assertEquals("medium", domain.thumbnailUrl)
    Assert.assertEquals("alt", domain.alt)
  }

  @Test
  fun `mapEntityToDomain maps correctly`() {
    val entity = PhotoEntity(
      id = 1,
      width = 100,
      height = 200,
      url = "url",
      photographer = "Dani",
      photographerUrl = "photographerUrl",
      photographerId = 1L,
      avgColor = "#00FF00",
      thumbnailUrl = "thumb",
      tinyThumbnailUrl = "tiny",
      largeImageUrl = "large",
      alt = "alt"
    )

    val domain = mapper.mapEntityToDomain(entity)

    Assert.assertEquals(1, domain.id)
    Assert.assertEquals(Color(0xFF00FF00), domain.avgColor)
    Assert.assertEquals("thumb", domain.thumbnailUrl)
  }

  @Test
  fun `mapDomainToEntity maps correctly`() {
    val domain = Photo(
      id = 1,
      type = "type",
      width = 100,
      height = 200,
      url = "url",
      photographer = "Dani",
      photographerUrl = "photographerUrl",
      photographerId = 1L,
      avgColor = Color.Companion.Blue,
      thumbnailUrl = "thumb",
      tinyThumbnailUrl = "tiny",
      largeImageUrl = "large",
      liked = true,
      alt = "alt"
    )

    val entity = mapper.mapDomainToEntity(domain)
    Assert.assertEquals(1, entity.id)
    Assert.assertNotNull(entity.avgColor)
    Assert.assertEquals("thumb", entity.thumbnailUrl)
  }
}