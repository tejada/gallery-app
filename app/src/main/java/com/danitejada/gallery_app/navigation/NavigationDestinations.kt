package com.danitejada.gallery_app.navigation

import kotlinx.serialization.Serializable

/**
 * Sealed interface representing all possible navigation destinations in the application.
 * This provides compile-time safety for navigation routes.
 */
sealed interface AppDestination

/**
 * Navigation destination for the API key screen.
 */
@Serializable
data object ApiKeyDestination: AppDestination

/**
 * Navigation destination for the photo list screen.
 */
@Serializable
data object PhotoListDestination: AppDestination

/**
 * Navigation destination for the photo detail screen.
 *
 * @property photoId The ID of the selected photo to display.
 */
@Serializable
data class PhotoDetailDestination(val photoId: Int): AppDestination