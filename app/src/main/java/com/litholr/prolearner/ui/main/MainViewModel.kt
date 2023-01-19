package com.litholr.prolearner.ui.main

import com.litholr.prolearner.data.local.LocalDbRepository
import com.litholr.prolearner.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val localDbRepository: LocalDbRepository
): BaseViewModel() {
    fun getLocalDb() = localDbRepository
}