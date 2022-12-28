package com.litholr.prolearner.ui.splash

import android.os.Bundle
import com.litholr.prolearner.R
import com.litholr.prolearner.databinding.FragmentSplashBinding
import com.litholr.prolearner.ui.base.BaseFragment

class SplashFragment : BaseFragment<FragmentSplashBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_splash

    override fun onCreateBegin(savedInstanceState: Bundle?) {
        setListeners()
    }

    private fun setListeners() {
        binding.searchStart.setOnClickListener {
            navigateTo(it, R.id.action_splashFragment_to_bookSearchFragment)
        }
    }
}