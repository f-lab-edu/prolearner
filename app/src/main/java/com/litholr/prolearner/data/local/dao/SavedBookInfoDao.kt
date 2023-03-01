package com.litholr.prolearner.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ejjang2030.bookcontentparser.api.naver.BookCatalog
import com.litholr.prolearner.data.local.entity.SavedBookInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedBookInfoDao {

    @Query("SELECT * FROM SavedBookInfo")
    fun getSavedBookInfoAll(): Flow<List<SavedBookInfo>>

    @Insert
    fun insertSavedBookInfo(savedBookInfo: SavedBookInfo)

    @Query("SELECT * FROM SavedBookInfo WHERE EXISTS (SELECT * FROM SavedBookInfo WHERE isbn = :isbn)")
    fun isBookExisted(isbn: String): Flow<Boolean>

    @Query("SELECT * FROM SavedBookInfo WHERE isbn = :isbn")
    fun getSavedBookInfoByIsbn(isbn: String): Flow<SavedBookInfo>

    @Query("UPDATE SavedBookInfo SET start_date = :startDate WHERE (isbn = :isbn)")
    fun updateStartDate(isbn: String, startDate: String)

    @Query("UPDATE SavedBookInfo SET end_date = :endDate WHERE (isbn = :isbn)")
    fun updateEndDate(isbn: String, endDate: String)
}