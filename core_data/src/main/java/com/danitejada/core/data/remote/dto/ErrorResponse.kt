package com.danitejada.core.data.remote.dto

import kotlinx.serialization.Serializable

/**
 * Represents the JSON structure of an error response from the Pexels API.
 */
@Serializable
data class ErrorResponse(
  val error: String? = null,
  val code: String? = null,
  val status: Int? = null
)