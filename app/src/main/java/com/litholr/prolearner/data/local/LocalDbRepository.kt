package com.litholr.prolearner.data.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.litholr.prolearner.data.local.dao.ContentInfoDao
import com.litholr.prolearner.data.local.dao.SavedBookInfoDao
import com.litholr.prolearner.data.local.entity.ContentInfo
import com.litholr.prolearner.data.local.entity.SavedBookInfo
import javax.inject.Inject

class LocalDbRepository @Inject constructor(
    private val savedBookInfoDao: SavedBookInfoDao,
    private val contentInfoDao: ContentInfoDao
) {
    fun getSavedBookInfoAll(): LiveData<List<SavedBookInfo>> = MutableLiveData(savedBookInfoDao.getAll())
    fun insertSavedBookInfo(vararg savedBookInfo: SavedBookInfo) = savedBookInfoDao.insert(*savedBookInfo)

    fun getContentList(isbn: String): LiveData<List<ContentInfo>> = MutableLiveData(contentInfoDao.getContentList(isbn))
    fun insertContents(vararg contentInfo: ContentInfo) = contentInfoDao.insertContents(*contentInfo)
    fun deleteContent(contentId: Int) = contentInfoDao.deleteContent(contentId)
    fun modifyContentPosition(
        isbn: String,
        contentId: Int,
        parentId: Int,
        orderNumber: Int
    ) {
        contentInfoDao.modifyContentPosition(
            isbn, contentId, parentId, orderNumber
        )
    }
    fun modifyContentTitle(
        isbn: String,
        contentId: Int,
        contentTitle: String
    ) {
        contentInfoDao.modifyContentTitle(
            isbn,
            contentId,
            contentTitle
        )
    }
    fun getContentListFromParentOf(
        parentId: Int?
    ): LiveData<List<ContentInfo>> = MutableLiveData(contentInfoDao.getContentListFromParentOf(parentId))
    fun modifyContentIdentifier(
        isbn: String,
        contentId: Int,
        contentIdentifier: String
    ) {
        contentInfoDao.modifyContentIdentifier(
            isbn, contentId, contentIdentifier
        )
    }
}