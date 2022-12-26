package com.litholr.prolearner.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import com.litholr.prolearner.databinding.ActivityMainBinding
import com.litholr.prolearner.ui.base.BaseActivity
import com.litholr.prolearner.utils.PLToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {
    override val binding: ActivityMainBinding
        get() = ActivityMainBinding.inflate(LayoutInflater.from(this))
    override val viewModel: MainViewModel
        get() = MainViewModel()

    override fun onCreateBegin(savedInstanceState: Bundle?) {
        PLToast.makeToast(this, "안녕하세요.", Toast.LENGTH_SHORT)
    }
}