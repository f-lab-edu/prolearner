package com.litholr.prolearner.manager

import android.content.Context

object AppManager {
    private lateinit var _applicationContext: Context
    val applicationContext: Context
        get() = _applicationContext

    fun initialize(appContext: Context) {
        this._applicationContext = appContext
    }
}