package com.danitejada.core.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.danitejada.core.data.local.dao.PhotoDao
import com.danitejada.core.data.local.entities.PhotoEntity

@Database(
  entities = [PhotoEntity::class],
  version = 1,
  exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
  abstract fun photoDao(): PhotoDao
}
