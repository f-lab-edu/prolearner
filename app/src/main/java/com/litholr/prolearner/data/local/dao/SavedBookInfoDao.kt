package com.litholr.prolearner.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.litholr.prolearner.data.local.entity.SavedBookInfo

@Dao
interface SavedBookInfoDao {

    @Query("SELECT * FROM SavedBookInfo")
    fun getAll(): List<SavedBookInfo>

    @Insert
    fun insert(vararg savedBookInfo: SavedBookInfo)
}