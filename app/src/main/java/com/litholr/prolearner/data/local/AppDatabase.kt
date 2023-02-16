package com.litholr.prolearner.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.litholr.prolearner.data.local.dao.ContentInfoDao
import com.litholr.prolearner.data.local.dao.SavedBookInfoDao
import com.litholr.prolearner.data.local.entity.ContentInfo
import com.litholr.prolearner.data.local.entity.SavedBookInfo

@Database(entities = [SavedBookInfo::class, ContentInfo::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
//    abstract fun savedBookInfoDao(): SavedBookInfoDao
//    abstract fun contentInfoDao(): ContentInfoDao
}