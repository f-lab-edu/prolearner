package com.litholr.prolearner.ui.base

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.viewbinding.ViewBinding

abstract class  BaseActivity<VB: ViewDataBinding, VM: BaseViewModel> : AppCompatActivity() {
    abstract val layoutId: Int
    lateinit var binding : VB
    abstract val viewModel : VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutId)
        setContentView(binding.root)
        onCreateBegin(savedInstanceState)
    }

    abstract fun onCreateBegin(savedInstanceState: Bundle?)
}