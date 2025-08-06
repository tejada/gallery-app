package com.danitejada.gallery_app.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.danitejada.feature.photos.photos.detail.PhotoDetailScreen
import com.danitejada.feature.photos.photos.list.PhotoListScreen
import com.danitejada.feature.settings.apikey.ApiKeyScreen

/**
 * Sets up the main navigation graph of the app using Jetpack Navigation and Hilt.
 *
 * @param startDestination The initial destination to launch (e.g., [ApiKeyDestination] or [PhotoListDestination]).
 */
@Composable
fun AppNavigation(startDestination: AppDestination) {
  val navController = rememberNavController()

  NavHost(
    navController = navController,
    startDestination = startDestination
  ) {
    composable<ApiKeyDestination> {
      ApiKeyScreen(
        viewModel = hiltViewModel(),
        onApiKeySaved = {
          navController.navigate(PhotoListDestination) {
            popUpTo<ApiKeyDestination> { inclusive = true }
          }
        },
        onBackClick = {
          navController.popBackStack()
        },
        navController = navController
      )
    }

    composable<PhotoListDestination> {
      PhotoListScreen(
        viewModel = hiltViewModel(),
        onPhotoClick = { photoId ->
          navController.navigate(PhotoDetailDestination(photoId))
        },
        onSettingsClick = {
          navController.navigate(ApiKeyDestination)
        }
      )
    }

    composable<PhotoDetailDestination> { backStackEntry ->
      val args = backStackEntry.toRoute<PhotoDetailDestination>()
      PhotoDetailScreen(
        photoId = args.photoId,
        viewModel = hiltViewModel(),
        onBackClick = {
          navController.popBackStack()
        }
      )
    }
  }
}