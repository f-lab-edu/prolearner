package com.litholr.prolearner.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey @ColumnInfo(name = "user_id") val userId: Int,
    @ColumnInfo(name = "user_name") val userName: String,
    @ColumnInfo(name = "user_email") val userEmail: String,
    @ColumnInfo(name = "user_nickname") val nickname: String?
)