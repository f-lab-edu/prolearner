package com.litholr.prolearner.ui.book

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.litholr.prolearner.R
import com.litholr.prolearner.databinding.BookContentItemBinding
import com.litholr.prolearner.databinding.FragmentContentsBinding
import com.litholr.prolearner.databinding.FragmentGoalsBinding
import com.litholr.prolearner.ui.base.BaseFragment

class GoalsFragment(): BaseFragment<FragmentGoalsBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_goals

    override fun onCreateBegin(savedInstanceState: Bundle?) {

    }
}