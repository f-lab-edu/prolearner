package com.litholr.prolearner.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.litholr.prolearner.data.local.dao.UserDao
import com.litholr.prolearner.data.local.entity.User

@Database(entities = [User::class, ], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}