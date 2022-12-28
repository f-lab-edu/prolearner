package com.litholr.prolearner.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.viewbinding.ViewBinding
import com.litholr.prolearner.R
import com.litholr.prolearner.databinding.FragmentSplashBinding

abstract class BaseFragment<VB: ViewDataBinding>: Fragment() {
    abstract val layoutId: Int
    lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        onCreateBegin(savedInstanceState)
        return binding.root
    }

    abstract fun onCreateBegin(savedInstanceState: Bundle?)

    fun navigateTo(view: View, actionId: Int) {
        view.findNavController().navigate(actionId)
    }
}