package com.litholr.prolearner.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.litholr.prolearner.data.local.entity.SearchHistory

@Dao
interface SearchHistoryDao {
    @Query("SELECT * FROM searchHistory")
    fun getAll(): List<SearchHistory>

    @Insert
    fun insert(searchHistory: SearchHistory)
}