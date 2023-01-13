package com.bgrem.data.db.converter

import androidx.room.TypeConverter
import com.bgrem.data.db.background.model.BackgroundGroupDb

class BackgroundGroupTypeConverter {
    @TypeConverter
    fun toDb(value: BackgroundGroupDb): String = value.name

    @TypeConverter
    fun fromDb(value: String): BackgroundGroupDb = BackgroundGroupDb.valueOf(value)
}