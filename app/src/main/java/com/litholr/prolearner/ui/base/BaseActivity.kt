package com.litholr.prolearner.ui.base

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewbinding.ViewBinding

abstract class  BaseActivity<VB: ViewBinding, VM: BaseViewModel> : AppCompatActivity() {
    abstract val binding : VB
    abstract val viewModel : VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        onCreateBegin(savedInstanceState)
    }

    abstract fun onCreateBegin(savedInstanceState: Bundle?)
}