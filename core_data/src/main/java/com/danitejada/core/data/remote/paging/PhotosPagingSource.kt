package com.danitejada.core.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.danitejada.core.data.local.dao.PhotoDao
import com.danitejada.core.data.mappers.PhotoMapper
import com.danitejada.core.data.remote.api.PhotosApi
import com.danitejada.core.domain.models.Photo
import com.danitejada.core.domain.repositories.SettingsRepository
import java.io.IOException

class PhotosPagingSource(
  private val photosApi: PhotosApi,
  private val settingsRepository: SettingsRepository,
  private val photoMapper: PhotoMapper,
  private val photoDao: PhotoDao
) : PagingSource<Int, Photo>() {

  override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
    val page = params.key ?: 1
    return try {
      val apiKey = settingsRepository.getApiKey()?.value
        ?: return LoadResult.Error(Exception("API key not found"))

      val response = photosApi.getPhotos(apiKey, page, params.loadSize)
      val photos = response.photos.map { photoMapper.mapDtoToDomain(it) }

      if (page == 1) {
        photoDao.clearAllPhotos()
      }
      photoDao.insertPhotos(photos.map { photoMapper.mapDomainToEntity(it) })

      LoadResult.Page(
        data = photos,
        prevKey = if (page == 1) null else page - 1,
        nextKey = if (response.nextPage == null) null else page + 1
      )
    } catch (exception: IOException) {
      return LoadResult.Error(exception)
    }
  }

  override fun getRefreshKey(state: PagingState<Int, Photo>): Int? {
    return state.anchorPosition?.let { anchorPosition ->
      state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
        ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
    }
  }
}