package com.litholr.prolearner.data.local

import androidx.annotation.WorkerThread
import com.litholr.prolearner.data.local.dao.ContentInfoDao
import com.litholr.prolearner.data.local.dao.SavedBookInfoDao
import com.litholr.prolearner.data.local.entity.ContentInfo
import com.litholr.prolearner.data.local.entity.SavedBookInfo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AppDBRepository @Inject constructor(
    private val savedBookInfoDao: SavedBookInfoDao,
    private val contentInfoDao: ContentInfoDao)
{
    // SavedBookInfoDao
    fun getSavedBookInfoAll(): Flow<List<SavedBookInfo>> = savedBookInfoDao.getSavedBookInfoAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertSavedBookInfo(savedBookInfo: SavedBookInfo) {
        savedBookInfoDao.insertSavedBookInfo(savedBookInfo)
    }

    fun isBookExisted(isbn: String): Flow<Boolean> = savedBookInfoDao.isBookExisted(isbn)
    fun getSavedBookInfoByIsbn(isbn: String): Flow<SavedBookInfo> = savedBookInfoDao.getSavedBookInfoByIsbn(isbn)

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updateStartDate(isbn: String, startDate: String) {
        savedBookInfoDao.updateStartDate(isbn, startDate)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updateEndDate(isbn: String, endDate: String) {
        savedBookInfoDao.updateEndDate(isbn, endDate)
    }

    // ContentInfoDao
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertContent(contentInfo: ContentInfo) {
        contentInfoDao.insertContent(contentInfo)
    }

    fun getContentList(isbn: String): Flow<List<ContentInfo>> = contentInfoDao.getContentList(isbn)


    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updateChecked(isbn: String, contentSortNumber: Int, isChecked: Boolean) {
        contentInfoDao.updateChecked(isbn, contentSortNumber, isChecked)
    }
}