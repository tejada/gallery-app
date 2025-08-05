package com.danitejada.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.danitejada.core.data.local.entities.PhotoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoDao {

  @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
  suspend fun insertPhotos(photos: List<PhotoEntity>)

  @Query("SELECT * FROM photos ORDER BY id DESC")
  fun getAllPhotos(): Flow<List<PhotoEntity>>

  @Query("SELECT * FROM photos WHERE id = :photoId")
  suspend fun getPhotoById(photoId: Int): PhotoEntity?

  @Query("DELETE FROM photos")
  suspend fun clearAllPhotos()
}