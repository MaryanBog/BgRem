package com.bgrem.data.db.common

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bgrem.data.db.background.BackgroundDao
import com.bgrem.data.db.background.model.BackgroundEntity
import com.bgrem.data.db.converter.BackgroundGroupTypeConverter
import com.bgrem.data.db.converter.BackgroundTypeConverter

@TypeConverters(BackgroundGroupTypeConverter::class, BackgroundTypeConverter::class)
@Database(
    entities = [BackgroundEntity::class],
    version = 2,
    exportSchema = true,
    autoMigrations = [AutoMigration(from = 1, to = 2)]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getBackgroundDao(): BackgroundDao
}