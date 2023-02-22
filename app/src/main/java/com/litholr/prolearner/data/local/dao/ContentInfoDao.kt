package com.litholr.prolearner.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.litholr.prolearner.data.local.entity.ContentInfo

@Dao
interface ContentInfoDao {

    @Insert
    fun insertContent(contentInfo: ContentInfo)

    @Query("SELECT * FROM ContentInfo WHERE isbn = :isbn ORDER BY content_sort_number")
    fun getContentList(isbn: String): List<ContentInfo>

    @Query("UPDATE ContentInfo SET isChecked = :isChecked WHERE (isbn = :isbn AND content_sort_number = :contentSortNumber)")
    fun updateChecked(isbn: String, contentSortNumber: Int, isChecked: Boolean)

//    @Query("UPDATE ContentInfo SET parent_content_id = :parentId AND content_sort_number = :orderNumber WHERE isbn = :isbn AND content_id = :contentId")
//    fun modifyContentPosition(isbn: String, contentId: Int, parentId: Int, orderNumber: Int)
//
//    @Query("UPDATE ContentInfo SET content_title = :contentTitle WHERE isbn = :isbn AND content_id = :contentId")
//    fun modifyContentTitle(isbn: String, contentId: Int, contentTitle: String)
//
//    @Query("UPDATE ContentInfo SET content_identifier = :contentIdentifier WHERE isbn = :isbn AND content_id = :contentId")
//    fun modifyContentIdentifier(isbn: String, contentId: Int, contentIdentifier: String)
//
//    @Query("SELECT * FROM ContentInfo WHERE parent_content_id = :parentId")
//    fun getContentListFromParentOf(parentId: Int?): List<ContentInfo>
//

//
//    @Query("DELETE FROM ContentInfo WHERE content_id = :contentId")
//    fun deleteContent(contentId: Int)
}