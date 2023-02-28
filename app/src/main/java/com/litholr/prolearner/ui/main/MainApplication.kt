package com.litholr.prolearner.ui.main

import android.app.Application
import com.litholr.prolearner.data.local.AppDBRepository
import com.litholr.prolearner.data.local.AppDatabase
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

@HiltAndroidApp
class MainApplication: Application() {
    val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { AppDatabase.getDB(this) }

    val appDBRepository by lazy { AppDBRepository(database.savedBookInfoDao(), database.contentInfoDao()) }
}