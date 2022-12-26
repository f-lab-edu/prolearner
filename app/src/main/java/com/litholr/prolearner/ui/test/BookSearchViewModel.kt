package com.litholr.prolearner.ui.test

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import api.naver.BookResult
import api.naver.BookSearchResult
import api.naver.NaverSearching
import com.litholr.prolearner.utils.SecretId

class BookSearchViewModel : ViewModel() {
    var query = MutableLiveData("")
    var isInitial = MutableLiveData(true)
    var display = MutableLiveData(10)
    var page = MutableLiveData(1)
    var naver = NaverSearching(SecretId.NAVER_CLIENT_ID, SecretId.NAVER_CLIENT_ID_SECRET)
    var results = MutableLiveData("")
    var books = MutableLiveData<ArrayList<BookResult>>(ArrayList())


    fun searchBook(query: String, display: Int, page: Int) {
        if(query != this.query.value) {
            this.query.postValue(query)
            isInitial.postValue(true)
        }
        naver.searchBook(query, display, page) { call, response, t ->
            if(response != null) {
                if(response.isSuccessful) {
                    var result = response.body()
                    Log.d(this.javaClass.simpleName, "search : $result")
                    results.postValue(result.toString())
                    if(result != null) {
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