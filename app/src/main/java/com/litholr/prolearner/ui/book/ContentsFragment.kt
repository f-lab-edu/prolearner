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
import com.litholr.prolearner.ui.base.BaseFragment

class ContentsFragment(val contentsList: List<String>): BaseFragment<FragmentContentsBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_contents

    override fun onCreateBegin(savedInstanceState: Bundle?) {
        binding.bookContents.apply {
            adapter = BookContentAdapter(contentsList)
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
    }

    inner class BookContentAdapter(val array: List<String> = ArrayList()) : RecyclerView.Adapter<BookContentViewHolder>() {

        val map = mutableMapOf<Int, MutableLiveData<Boolean>>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookContentViewHolder {
            return BookContentViewHolder(BookContentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }

        override fun onBindViewHolder(holder: BookContentViewHolder, position: Int) {
            holder.bindItem(array[position])
            map[position] = MutableLiveData(false)
            holder.binding().check.setOnClickListener {
                map[position]!!.postValue(!it.isSelected)
                it.isSelected = !it.isSelected
            }
        }

        override fun getItemCount(): Int = array.size
    }

    inner class BookContentViewHolder(private val bookContentItemBinding: BookContentItemBinding): RecyclerView.ViewHolder(bookContentItemBinding.root) {
        fun bindItem(title: String) {
            bookContentItemBinding.contentTitle.text = title
        }
        fun binding(): BookContentItemBinding {
            return bookContentItemBinding
        }
    }
}