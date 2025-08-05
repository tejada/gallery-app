package com.danitejada.core.data.remote.service

import com.danitejada.core.data.remote.api.PhotosApi
import com.danitejada.core.data.remote.dto.PhotoDto
import com.danitejada.core.data.remote.dto.PhotosResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.http.HttpHeaders
import javax.inject.Inject

class PhotosService @Inject constructor(private val httpClient: HttpClient) : PhotosApi {

  override suspend fun getPhotos(apiKey: String, page: Int?, perPage: Int?): PhotosResponseDto {
    return httpClient.get("v1/curated") {
      headers {
        append(HttpHeaders.Authorization, apiKey)
      }
      parameter("page", page)
      parameter("per_page", perPage)
    }.body()
  }

  override suspend fun getPhoto(apiKey: String, id: Int): PhotoDto {
    return httpClient.get("v1/photos/$id") {
      headers {
        append(HttpHeaders.Authorization, apiKey)
      }
    }.body()
  }
} 