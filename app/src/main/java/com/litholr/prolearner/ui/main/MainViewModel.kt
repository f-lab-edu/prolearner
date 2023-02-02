package com.litholr.prolearner.ui.main

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import api.naver.BookResult
import api.naver.NaverSearching
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.litholr.prolearner.R
import com.litholr.prolearner.ui.base.BaseViewModel
import com.litholr.prolearner.utils.SecretId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlin.collections.ArrayList

@HiltViewModel
class MainViewModel: BaseViewModel() {
    var query = MutableLiveData("")
    var isInitial = MutableLiveData(true)
    var page = MutableLiveData(1)
    var naver = NaverSearching(SecretId.NAVER_CLIENT_ID, SecretId.NAVER_CLIENT_ID_SECRET)
    var results = MutableLiveData("")
    var books = MutableLiveData<ArrayList<BookResult>>(ArrayList())
    var selectedBook = MutableLiveData<BookResult>()

    var plToastListener: PLToastListener? = null

    fun searchBook() {
        naver.searchBook(query.value!!, 10, page.value!!, "sim") { call, response, t ->
            if(response != null) {
                if(response.isSuccessful) {
                    val result = response.body()
                    if(result == null) {
                        plToastListener?.printToast("검색결과가 없습니다.")
                        return@searchBook
                    }
                    Log.d(this.javaClass.simpleName, "search : $result")
                    results.postValue(result.toString())
                    result.let {
                        if(!it.items.none()) {
                            if(isInitial.value == true) {
                                isInitial.postValue(false)
                                books.postValue(ArrayList(result.items))
                            } else {
                                val old = books.value!!
                                val array = ArrayList<BookResult>()
                                array.addAll(old)
                                array.addAll(ArrayList(result.items))
                                books.postValue(array)
                            }
                        }
                    }
                }
            }
        }
    }
    enum class BottomNav { HOME, SEARCH, PROFILE, BOOK, NONE }
    val bottomNav = MutableLiveData(BottomNav.HOME)
    fun setOnNavigationItemSelectedListener(bottomNavigationView: BottomNavigationView) {
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.home -> {
                    bottomNav.value = BottomNav.HOME
                    true
                }
                R.id.search -> {
                    bottomNav.value = BottomNav.SEARCH
                    true
                }
                R.id.profile -> {
                    bottomNav.value = BottomNav.PROFILE
                    true
                }
                else -> false
            }
        }
    }

    fun onSearchButtonClick() {
        isInitial.postValue(true)
        searchBook()
    }

    fun toSearchBack() {
        bottomNav.value = BottomNav.SEARCH
    }

    fun setPLToastListener(plToastListener: PLToastListener) {
        this.plToastListener = plToastListener
    }

    interface PLToastListener {
        fun printToast(message: String, term: Int = Toast.LENGTH_SHORT)
    }
}