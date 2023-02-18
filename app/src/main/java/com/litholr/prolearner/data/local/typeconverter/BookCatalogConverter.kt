package com.litholr.prolearner.data.local.typeconverter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.ejjang2030.bookcontentparser.api.naver.BookCatalog
import com.google.gson.Gson

@ProvidedTypeConverter
class BookCatalogConverter(private val gson: Gson) {
    @TypeConverter
    fun bookCatalogToJson(value: BookCatalog): String? {
        return gson.toJson(value)
    }

    @TypeConverter
    fun jsonToBookCatalog(value: String): BookCatalog {
        return gson.fromJson(value, BookCatalog::class.java)
    }
}