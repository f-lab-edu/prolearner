package com.litholr.prolearner.ui.main

import androidx.lifecycle.LiveData
import androidx.room.Room
import androidx.room.RoomDatabase
import com.litholr.prolearner.data.local.AppDatabase
import com.litholr.prolearner.data.local.LocalDbRepository
import com.litholr.prolearner.data.local.entity.ContentInfo
import com.litholr.prolearner.data.local.entity.SavedBookInfo
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import api.naver.BookResult
import api.naver.BookSearchResult
import api.naver.NaverSearching
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.litholr.prolearner.R
import com.litholr.prolearner.ui.base.BaseViewModel
import com.litholr.prolearner.utils.SecretId
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject  
import kotlin.collections.ArrayList

@HiltViewModel
class MainViewModel @Inject constructor(
    private val localDbRepository: LocalDbRepository
): BaseViewModel() {
    var naver = NaverSearching(SecretId.NAVER_CLIENT_ID, SecretId.NAVER_CLIENT_ID_SECRET)
    private val searchResultItemCount = 10

    private val _query = MutableLiveData("")
    val query: LiveData<String?>
        get() = _query

    private val _page = MutableLiveData(1)
    val page: LiveData<Int>
        get() = _page

    private val _books = MutableLiveData<ArrayList<BookResult>>(ArrayList())
    val books: MutableLiveData<ArrayList<BookResult>>
        get() = _books

    var results = MutableLiveData("")

    var selectedBook = MutableLiveData<BookResult>()

    val _bookResultState = MutableLiveData(BookResultState.BEFORE_SEARCH)
    val bookResultState: LiveData<BookResultState>
        get() = _bookResultState

    enum class BottomNav { HOME, SEARCH, PROFILE, BOOK, NONE }
    val _bottomNav = MutableLiveData(BottomNav.HOME)
    val bottomNav: LiveData<BottomNav>
        get() = _bottomNav

    fun searchBook() {
        naver.searchBook(query.value!!, 10, page.value!!, "sim") { call, response, t ->
            if(response != null) {
                if(response.isSuccessful) {
                    val result = response.body()
                    if(result == null) {
                        this.showToastNullOfSearchResult()
                        return@searchBook
                    }
                    Log.d(this.javaClass.simpleName, "search : $result")
                    results.postValue(result.toString())
                    result.let { bookSearchResult ->
                        Log.d(this.javaClass.simpleName, "$bookSearchResult")
                        val array = ArrayList<BookResult>()
                        books.value?.let { array.addAll(it) }
                        array.addAll(ArrayList(bookSearchResult.items))
                        books.postValue(array)
                    }
                }
            }
        }
    }

    fun setOnNavigationItemSelectedListener(bottomNavigationView: BottomNavigationView) {
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.home -> {
                    _bottomNav.value = BottomNav.HOME
                    true
                }
                R.id.search -> {
                    _bottomNav.value = BottomNav.SEARCH
                    true
                }
                R.id.profile -> {
                    _bottomNav.value = BottomNav.PROFILE
                    true
                }
                else -> false
            }
        }
    }

    fun onSearchButtonClick(query: String) {
        updateQuery(query)
        searchBook()
    }

    fun toSearchBack() {
        _bottomNav.value = BottomNav.SEARCH
    }

    enum class BookResultState { BEFORE_SEARCH, SEARCH_RESULT_NULL, BOOK_INFO_NULL, BOOK_CONTENTS_NULL }

    fun showToastNullOfSearchResult() {
        this._bookResultState.postValue(BookResultState.SEARCH_RESULT_NULL)
    }
    fun showToastNullOfBookInfo() {
        this._bookResultState.postValue(BookResultState.BOOK_INFO_NULL)
        this._bottomNav.postValue(BottomNav.SEARCH)
    }
    fun showToastNullOfBookContents() {
        this._bookResultState.postValue(BookResultState.BOOK_CONTENTS_NULL)
    }
    fun updateQuery(query: String?) {
        this._books.value!!.clear()
        this._query.postValue(query.toString())
        this._page.postValue(1)
    }
    fun updateBottomNavToBook() {
        this._bottomNav.postValue(BottomNav.BOOK)
    }
    fun updateScrollPage() {
        this._page.postValue(this._page.value!!.plus(1))
        searchBook()
    }
    fun getSearchResultItemCount(): Int {
        return this.searchResultItemCount
    }
    
    // for room db
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