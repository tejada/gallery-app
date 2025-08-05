package com.danitejada.gallery_app.navigation

import kotlinx.serialization.Serializable

@Serializable
data object ApiKeyDestination

@Serializable
data object PhotoListDestination

@Serializable
data class PhotoDetailDestination(val photoId: Int)