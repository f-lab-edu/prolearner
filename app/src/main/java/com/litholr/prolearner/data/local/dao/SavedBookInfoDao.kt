package com.litholr.prolearner.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ejjang2030.bookcontentparser.api.naver.BookCatalog
import com.litholr.prolearner.data.local.entity.SavedBookInfo

@Dao
interface SavedBookInfoDao {

    @Query("SELECT * FROM SavedBookInfo")
    fun getSavedBookInfoAll(): List<SavedBookInfo>

    @Insert
    fun insertSavedBookInfo(savedBookInfo: SavedBookInfo)

    @Query("SELECT * FROM SavedBookInfo WHERE EXISTS (SELECT * FROM SavedBookInfo WHERE isbn = :isbn)")
    fun isBookExisted(isbn: String): Boolean
//
//    @Insert
//    fun initBookCatalog(catalog: BookCatalog)
//
    @Query("SELECT * FROM SavedBookInfo WHERE isbn = :isbn")
    fun getSavedBookInfoByIsbn(isbn: String): SavedBookInfo
}