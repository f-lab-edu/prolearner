package com.litholr.prolearner.ui.main

import android.content.Context
import com.litholr.prolearner.data.local.entity.SavedBookInfo
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.room.Room
import api.naver.BookResult
import api.naver.NaverSearching
import com.ejjang2030.bookcontentparser.api.naver.BookCatalog
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.litholr.prolearner.R
import com.litholr.prolearner.data.local.AppDatabase
import com.litholr.prolearner.data.local.typeconverter.BookCatalogConverter
import com.litholr.prolearner.data.local.typeconverter.BookResultConverter
import com.litholr.prolearner.ui.base.BaseViewModel
import com.litholr.prolearner.utils.PLToast
import com.litholr.prolearner.utils.SecretId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.collections.ArrayList

@HiltViewModel
class MainViewModel : BaseViewModel() {
    var db: AppDatabase? = null

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

    private val _checkedContents = MutableLiveData<MutableMap<Int, Pair<String, Boolean>>>()
    val checkedContents: LiveData<MutableMap<Int, Pair<String, Boolean>>>
        get() = _checkedContents

    var results = MutableLiveData("")

    private val _selectedBook = MutableLiveData<BookResult>()
    val selectedBook: LiveData<BookResult>
        get() = _selectedBook

    private val _selectedBookCatalog = MutableLiveData<BookCatalog>()
    val selectedBookCatalog: MutableLiveData<BookCatalog>
        get() = _selectedBookCatalog


    val _bookResultState = MutableLiveData(BookResultState.BEFORE_SEARCH)
    val bookResultState: LiveData<BookResultState>
        get() = _bookResultState

    enum class BottomNav { HOME, SEARCH, PROFILE, BOOK, NONE }
    val _bottomNav = MutableLiveData(BottomNav.HOME)
    val bottomNav: LiveData<BottomNav>
        get() = _bottomNav

    var savedBookInfo: SavedBookInfo? = null

    var _isSavedBookPage = MutableLiveData<Boolean>()
    val isSavedBookPage: LiveData<Boolean>
        get() = _isSavedBookPage

    fun initDB(context: Context) {
        val gson = Gson()
        db = Room
            .databaseBuilder(context, AppDatabase::class.java, "prolearner_app.db")
            .addTypeConverter(BookCatalogConverter(gson))
            .addTypeConverter(BookResultConverter(gson))
            .fallbackToDestructiveMigration()
            .build()
    }

    fun searchBook() {
        naver.searchBook(query.value!!, 10, page.value!!, "sim") { call, response, t ->
            if(response != null) {
                if(response.isSuccessful) {
                    val result = response.body()
                    if(result == null) {
                        this.showToastNullOfSearchResult()
                        return@searchBook
                    }
                    results.postValue(result.toString())
                   // Log.d(this.javaClass.simpleName, "page : ${page.value}")
                    //Log.d(this.javaClass.simpleName, "result : ${result.items.map { it.title }}")
                    result.let { bookSearchResult ->
//                        Log.d(this.javaClass.simpleName, "$bookSearchResult")
//                        val array = ArrayList<BookResult>()
//                        books.value?.let { array.addAll(it) }
//                        array.addAll(ArrayList(bookSearchResult.items))
                        books.postValue(ArrayList(bookSearchResult.items))
                    }
                }
            }
        }
    }

    fun saveBook(context: Context) {
        if(savedBookInfo != null) {
            CoroutineScope(Dispatchers.IO).launch {
                insertSavedBookInfo(savedBookInfo!!)
            }
            PLToast.makeToast(context, "책이 저장되었습니다.")
            this._bottomNav.postValue(BottomNav.HOME)
        }
    }

    fun updateCheckedContent(index: Int, isChecked: Boolean) {
        val curr: Pair<String, Boolean>? = _checkedContents.value?.get(index)
        val contentTitle = curr?.first ?: ""
        val new: Pair<String, Boolean> = Pair(contentTitle, isChecked)
        _checkedContents.value?.put(index, new)
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

    fun toBack() {
        if(_isSavedBookPage.value!!) {
            _bottomNav.value = BottomNav.HOME
        } else {
            _bottomNav.value = BottomNav.SEARCH
        }
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
    fun updateBottomNavToBook(bookResult: BookResult, isFirst: Boolean = true) {
        this._isSavedBookPage.value = !isFirst
        this._selectedBook.postValue(bookResult)
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
    // SavedBookInfo
    fun getSavedBookInfos(): LiveData<List<SavedBookInfo>> {
        return liveData {
            val data = withContext(Dispatchers.IO) {
                db!!.savedBookInfoDao().getSavedBookInfoAll()
            }
            emit(data)
        }
    }
    suspend fun isBookExisted(isbn: String): Boolean {
        return withContext(Dispatchers.IO) {
            db!!.savedBookInfoDao().isBookExisted(isbn)
        }
    }
    suspend fun insertSavedBookInfo(savedBookInfo: SavedBookInfo) {
        return withContext(Dispatchers.IO) {
            db!!.savedBookInfoDao().insertSavedBookInfo(savedBookInfo)
        }
    }
    suspend fun getSavedBookInfoByISBN(isbn: String): SavedBookInfo {
        return withContext(Dispatchers.IO) {
            db!!.savedBookInfoDao().getSavedBookInfoByIsbn(isbn)
        }
    }
}