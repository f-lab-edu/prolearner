package com.litholr.prolearner.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.litholr.prolearner.data.local.dao.ContentInfoDao
import com.litholr.prolearner.data.local.dao.SavedBookInfoDao
import com.litholr.prolearner.data.local.entity.ContentInfo
import com.litholr.prolearner.data.local.entity.SavedBookInfo
import com.litholr.prolearner.data.local.typeconverter.BookCatalogConverter
import com.litholr.prolearner.data.local.typeconverter.BookResultConverter

@Database(entities = [SavedBookInfo::class, ContentInfo::class], version = 3, exportSchema = true)
@TypeConverters(value = [BookCatalogConverter::class, BookResultConverter::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun savedBookInfoDao(): SavedBookInfoDao
    abstract fun contentInfoDao(): ContentInfoDao
}