package com.litholr.prolearner.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import api.naver.BookResult
import com.ejjang2030.bookcontentparser.api.naver.BookCatalog

@Entity(tableName="SavedBookInfo")
data class SavedBookInfo(
    @PrimaryKey @ColumnInfo(name = "isbn") val isbn: String, // isbn
    @ColumnInfo(name = "start_date") val startDate: String?,
    @ColumnInfo(name = "end_date") val endDate: String?,
    @ColumnInfo(name = "catalog") val catalog: BookCatalog?,
    @ColumnInfo(name = "bookResult") val bookResult: BookResult
)