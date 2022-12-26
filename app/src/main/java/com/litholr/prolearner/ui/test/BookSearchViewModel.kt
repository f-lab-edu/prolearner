package com.litholr.prolearner.ui.test

import SecretId
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import api.naver.BookSearchResult
import api.naver.NaverSearching

class BookSearchViewModel : ViewModel() {
    var query = MutableLiveData("")
    var display = MutableLiveData(10)
    var page = MutableLiveData(1)
    var naver = NaverSearching(SecretId.NAVER_CLIENT_ID, SecretId.NAVER_CLIENT_ID_SECRET)
    var results = MutableLiveData("")
    fun searchBook(query: String, display: Int, page: Int) {
        naver.searchBook(query, display, page) { call, response, t ->
            if(response != null) {
                if(response.isSuccessful) {
                    var result = response.body()
                    Log.d(this.javaClass.simpleName, "search : $result")
                    results.postValue(response.toString())
                }
            }
        }
    }
}