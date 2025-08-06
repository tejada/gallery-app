package com.danitejada.core.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.danitejada.core.data.local.dao.PhotoDao
import com.danitejada.core.data.local.entities.PhotoEntity

/**
 * Room database for storing photo-related data.
 */
@Database(
  entities = [PhotoEntity::class],
  version = 1,
  exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

  /**
   * Provides access to the [PhotoDao] for photo database operations.
   *
   * @return The [PhotoDao] instance.
   */
  abstract fun photoDao(): PhotoDao
}
