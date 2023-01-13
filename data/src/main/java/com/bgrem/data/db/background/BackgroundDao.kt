package com.bgrem.data.db.background

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bgrem.data.db.background.model.BackgroundEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BackgroundDao {
    @Query("SELECT * FROM backgrounds")
    fun observeBackgrounds(): Flow<List<BackgroundEntity>>

    @Query("SELECT * FROM backgrounds WHERE `group` LIKE 'USER'")
    fun getUserBackgrounds(): List<BackgroundEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addBackground(background: BackgroundEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addBackgrounds(backgrounds: List<BackgroundEntity>)

    @Query("DELETE FROM backgrounds")
    fun clearAll()
}