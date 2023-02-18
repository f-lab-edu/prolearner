package com.litholr.prolearner.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="ContentInfo")
data class ContentInfo(
    @PrimaryKey @ColumnInfo(name = "content_id") val contentId: Int,
    @ColumnInfo(name = "isbn") val isbn: String,
    @ColumnInfo(name = "content_title") val contentTitle: String,
    @ColumnInfo(name = "content_sort_number") val contentSortNumber: Int,
    @ColumnInfo(name = "isChecked") val isChecked: Boolean
)
