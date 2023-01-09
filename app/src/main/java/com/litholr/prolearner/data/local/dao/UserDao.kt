package com.litholr.prolearner.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.litholr.prolearner.data.local.entity.User

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<User>

    @Query("SELECT * FROM user WHERE user_name LIMIT 1")
    fun findByUserName(userName: String): User

    @Query("SELECT * FROM user WHERE nickname LIMIT 1")
    fun findByNickname(nickname: String): User

    @Query("SELECT * FROM user WHERE email LIMIT 1")
    fun findByEmail(email: String): User

    @Insert
    fun insertAll(vararg users: User)

    @Delete
    fun delete(user: User)
}