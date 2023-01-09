package com.litholr.prolearner.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.litholr.prolearner.data.local.dao.ContentInfoDao
import com.litholr.prolearner.data.local.dao.SavedBookInfoDao
import com.litholr.prolearner.data.local.dao.SearchHistoryDao
import com.litholr.prolearner.data.local.dao.UserDao
import com.litholr.prolearner.data.local.entity.ContentInfo
import com.litholr.prolearner.data.local.entity.SavedBookInfo
import com.litholr.prolearner.data.local.entity.SearchHistory
import com.litholr.prolearner.data.local.entity.User

@Database(entities = [User::class, SearchHistory::class, SavedBookInfo::class, ContentInfo::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun searchHistoryDao(): SearchHistoryDao
    abstract fun savedBookInfoDao(): SavedBookInfoDao
    abstract fun contentInfoDao(): ContentInfoDao
}