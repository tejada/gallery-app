package com.danitejada.core.data.remote.api

import com.danitejada.core.data.remote.dto.PhotoDto
import com.danitejada.core.data.remote.dto.PhotosResponseDto

interface PhotosApi {

  suspend fun getPhotos(apiKey: String, page: Int?, perPage: Int?): PhotosResponseDto

  suspend fun getPhoto(apiKey: String, id: Int): PhotoDto
}