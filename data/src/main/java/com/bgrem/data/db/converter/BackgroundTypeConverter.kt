package com.bgrem.data.db.converter

import androidx.room.TypeConverter
import com.bgrem.data.db.background.model.BackgroundTypeDb

class BackgroundTypeConverter {
    @TypeConverter
    fun toDb(value: BackgroundTypeDb?): String? = value?.name

    @TypeConverter
    fun fromDb(value: String?): BackgroundTypeDb? = value?.let { BackgroundTypeDb.valueOf(it) }
}