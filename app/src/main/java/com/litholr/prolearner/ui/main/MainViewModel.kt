package com.litholr.prolearner.ui.main

import androidx.lifecycle.LiveData
import androidx.room.Room
import androidx.room.RoomDatabase
import com.litholr.prolearner.data.local.AppDatabase
import com.litholr.prolearner.data.local.LocalDbRepository
import com.litholr.prolearner.data.local.entity.ContentInfo
import com.litholr.prolearner.data.local.entity.SavedBookInfo
import com.litholr.prolearner.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val localDbRepository: LocalDbRepository
): BaseViewModel() {

    fun deleteContent(contentId: Int) {
        localDbRepository.deleteContent(contentId)
    }

    fun getContentList(isbn: String) {
        localDbRepository.getContentList(isbn)
    }

    fun insertContents(vararg contentInfo: ContentInfo) {
        localDbRepository.insertContents(*contentInfo)
    }

    fun getSavedBookInfoAll(): LiveData<List<SavedBookInfo>> {
        return localDbRepository.getSavedBookInfoAll()
    }

    fun insertSavedBookInfo(vararg savedBookInfo: SavedBookInfo) {
        localDbRepository.insertSavedBookInfo(*savedBookInfo)
    }

    fun modifyContentPosition(
        isbn: String,
        contentId: Int,
        parentId: Int,
        orderNumber: Int
    ) {
        localDbRepository.modifyContentPosition(isbn, contentId, parentId, orderNumber)
    }

    fun modifyContentTitle(
        isbn: String,
        contentId: Int,
        contentTitle: String
    ) {
        localDbRepository.modifyContentTitle(isbn, contentId, contentTitle)
    }

}