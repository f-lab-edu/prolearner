package com.litholr.prolearner.manager

import androidx.room.Room
import androidx.room.RoomDatabase
import com.litholr.prolearner.data.local.AppDatabase

object LocalDB {
    val db: RoomDatabase by lazy {
        Room.databaseBuilder(
            AppManager.applicationContext,
            AppDatabase::class.java, "localdb"
        ).build()
    }
}