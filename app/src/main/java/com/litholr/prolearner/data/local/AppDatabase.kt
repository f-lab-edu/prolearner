package com.litholr.prolearner.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.litholr.prolearner.data.local.dao.ContentInfoDao
import com.litholr.prolearner.data.local.dao.SavedBookInfoDao
import com.litholr.prolearner.data.local.entity.ContentInfo
import com.litholr.prolearner.data.local.entity.SavedBookInfo
import com.litholr.prolearner.data.local.typeconverter.BookCatalogConverter
import com.litholr.prolearner.data.local.typeconverter.BookResultConverter
import kotlinx.coroutines.CoroutineScope

@Database(entities = [SavedBookInfo::class, ContentInfo::class], version = 4, exportSchema = true)
// 정식 배포 버전에서는 exportSchema를 false를 사용하여 배포하지 않는 것이 좋다
@TypeConverters(value = [BookCatalogConverter::class, BookResultConverter::class])
abstract class AppDatabase : RoomDatabase() {

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDB(
            context: Context
        ): AppDatabase {
            val gson = Gson()
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "prolearner_app.db")
                    .addTypeConverter(BookCatalogConverter(gson))
                    .addTypeConverter(BookResultConverter(gson))
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    abstract fun savedBookInfoDao(): SavedBookInfoDao
    abstract fun contentInfoDao(): ContentInfoDao
}