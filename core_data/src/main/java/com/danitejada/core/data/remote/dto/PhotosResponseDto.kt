package com.danitejada.core.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents the JSON structure of a successful response from the Pexels API.
 */
@Serializable
data class PhotosResponseDto(
  val page: Int,
  @SerialName("per_page")
  val perPage: Int,
  val photos: List<PhotoDto>,
  @SerialName("total_results")
  val totalResults: Int,
  @SerialName("prev_page")
  val prevPage: String? = null,
  @SerialName("next_page")
  val nextPage: String? = null
)

@Serializable
data class PhotoDto(
  val type: String? = null,
  val id: Int,
  val width: Int? = null,
  val height: Int? = null,
  val url: String? = null,
  val photographer: String? = null,
  @SerialName("photographer_url")
  val photographerUrl: String? = null,
  @SerialName("photographer_id")
  val photographerId: Long? = null,
  @SerialName("avg_color")
  val avgColor: String? = null,
  val src: PhotoSourceDto? = null,
  val liked: Boolean? = null,
  val alt: String? = null
)

@Serializable
data class PhotoSourceDto(
  val original: String? = null,
  val large2x: String? = null,
  val large: String? = null,
  val medium: String? = null,
  val small: String? = null,
  val portrait: String? = null,
  val landscape: String? = null,
  val tiny: String? = null
)