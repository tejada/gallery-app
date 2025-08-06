package com.danitejada.core.data.remote.service

import com.danitejada.core.data.remote.api.PhotosApi
import com.danitejada.core.data.remote.dto.ErrorResponse
import com.danitejada.core.data.remote.dto.PhotoDto
import com.danitejada.core.data.remote.dto.PhotosResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess
import javax.inject.Inject

class PhotosService @Inject constructor(private val httpClient: HttpClient) : PhotosApi {

  override suspend fun getPhotos(apiKey: String, page: Int?, perPage: Int?): PhotosResponseDto {
    val response: HttpResponse = httpClient.get("v1/curated") {
      headers {
        append(HttpHeaders.Authorization, apiKey)
      }
      parameter("page", page)
      parameter("per_page", perPage)
    }

    if (response.status.isSuccess()) {
      return response.body()
    } else {
      throw parseError(response)
    }
  }

  override suspend fun getPhoto(apiKey: String, id: Int): PhotoDto {
    val response: HttpResponse = httpClient.get("v1/photos/$id") {
      headers {
        append(HttpHeaders.Authorization, apiKey)
      }
    }

    if (response.status.isSuccess()) {
      return response.body()
    } else {
      throw parseError(response)
    }
  }

  /**
   * Attempts to parse the body of a failed response into an [ErrorResponse]
   * to create a more specific exception message.
   */
  private suspend fun parseError(response: HttpResponse): Exception {
    return try {
      val errorResponse = response.body<ErrorResponse>()
      // Use the 'error' or 'code' field from the JSON for a better message.
      val errorMessage = errorResponse.error ?: errorResponse.code ?: "Unknown API Error"
      Exception("API Error: $errorMessage (Status: ${response.status.value})")
    } catch (e: Exception) {
      // This is a fallback in case the error body is not the expected JSON.
      Exception("API Error: Failed to parse error response. (Status: ${response.status.value})")
    }
  }
} 