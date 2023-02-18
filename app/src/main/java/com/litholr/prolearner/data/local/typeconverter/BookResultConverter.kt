package com.litholr.prolearner.data.local.typeconverter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import api.naver.BookResult
import com.google.gson.Gson

@ProvidedTypeConverter
class BookResultConverter(private val gson: Gson) {
    @TypeConverter
    fun bookResultToJson(value: BookResult): String? {
        return gson.toJson(value)
    }

    @TypeConverter
    fun jsonToBookResult(value: String): BookResult {
        return gson.fromJson(value, BookResult::class.java)
    }
}