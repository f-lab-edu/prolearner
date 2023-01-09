package com.litholr.prolearner.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SearchHistory(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "time") val time: String, // 검색 했을 시점(년월일-시분초)
    @ColumnInfo(name = "keyword") val keyword: String // 검색한 검색 키워드
    // 추후 검색 설정 컬럼 추가
)