package com.danitejada.core.domain.models

data class Photos(
  val photos: List<Photo> = emptyList(),
  val totalResults: Int = 0,
  val page: Int = 0,
  val perPage: Int = 0,
  val hasNextPage: Boolean
) 