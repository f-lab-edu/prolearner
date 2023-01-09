package com.litholr.prolearner.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ContentInfo(
    @PrimaryKey @ColumnInfo(name = "content_id") val contentId: Int,
    @ColumnInfo(name = "parent_content_id") val parentContentId: Int?, // 값이 없으면 최 상단 목차
    @ColumnInfo(name = "content_title") val contentTitle: String,
    @ColumnInfo(name = "content_identifier") val contentIdentifier: String?,
    @ColumnInfo(name = "content_sort_number") val contentSortNumber: Int,
    @ColumnInfo(name = "isbn") val isbn: String,
    @ColumnInfo(name = "saved_book_id") val savedBookId: Int
)
