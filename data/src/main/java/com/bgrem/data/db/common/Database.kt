package com.bgrem.data.db.common

import android.content.Context
import androidx.room.Room

object Database {
    fun build(context: Context):AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "app_database"
    ).build()
}