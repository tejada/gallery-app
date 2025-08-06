package com.danitejada.gallery_app.navigation

import kotlinx.serialization.Serializable

/**
 * Navigation destination for the API key screen.
 */
@Serializable
data object ApiKeyDestination

/**
 * Navigation destination for the photo list screen.
 */
@Serializable
data object PhotoListDestination

/**
 * Navigation destination for the photo detail screen.
 *
 * @property photoId The ID of the selected photo to display.
 */
@Serializable
data class PhotoDetailDestination(val photoId: Int)