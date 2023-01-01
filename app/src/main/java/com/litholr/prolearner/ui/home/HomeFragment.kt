package com.litholr.prolearner.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.litholr.prolearner.R
import com.litholr.prolearner.databinding.FragmentHomeBinding
import com.litholr.prolearner.ui.base.BaseFragment
import com.litholr.prolearner.ui.base.MainComposable

class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_home

    override fun onCreateBegin(savedInstanceState: Bundle?) {
        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MainComposable()
            }
        }
    }
}