package com.litholr.prolearner.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.litholr.prolearner.data.local.entity.ContentInfo

@Dao
interface ContentInfoDao {

    @Query("SELECT * FROM ContentInfo WHERE isbn IN (:isbn)")
    fun getContentList(isbn: String): List<ContentInfo>

    @Query("UPDATE ContentInfo SET parent_content_id = :parentId AND content_sort_number = :orderNumber WHERE isbn = :isbn AND content_id = :contentId")
    fun modifyContentPosition(isbn: String, contentId: Int, parentId: Int, orderNumber: Int)

    @Query("UPDATE ContentInfo SET content_title = :contentTitle WHERE isbn = :isbn AND content_id = :contentId")
    fun modifyContentTitle(isbn: String, contentId: Int, contentTitle: String)

    @Query("UPDATE ContentInfo SET content_identifier = :contentIdentifier WHERE isbn = :isbn AND content_id = :contentId")
    fun modifyContentIdentifier(isbn: String, contentId: Int, contentIdentifier: String)

    @Query("SELECT * FROM ContentInfo WHERE parent_content_id = :parentId")
    fun getContentListFromParentOf(parentId: Int?): List<ContentInfo>

    @Insert
    fun insertContents(vararg contentInfo: ContentInfo)

    @Query("DELETE FROM ContentInfo WHERE content_id = :contentId")
    fun deleteContent(contentId: Int)
}