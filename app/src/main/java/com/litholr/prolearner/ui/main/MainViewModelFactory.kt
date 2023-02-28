package com.litholr.prolearner.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.litholr.prolearner.data.local.AppDBRepository

class MainViewModelFactory(private val appDBRepository: AppDBRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(appDBRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}