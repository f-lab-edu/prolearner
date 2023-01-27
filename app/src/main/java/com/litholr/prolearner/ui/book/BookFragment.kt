package com.litholr.prolearner.ui.book

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import androidx.compose.ui.unit.dp
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import api.naver.BookResult
import com.bumptech.glide.Glide
import com.ejjang2030.bookcontentparser.BookContentParser
import com.litholr.prolearner.R
import com.litholr.prolearner.databinding.BookContentItemBinding
import com.litholr.prolearner.databinding.CardviewBookinfoBinding
import com.litholr.prolearner.databinding.FragmentBookBinding
import com.litholr.prolearner.ui.base.BaseFragment
import com.litholr.prolearner.ui.main.MainViewModel
import com.litholr.prolearner.utils.PLToast

class BookFragment: BaseFragment<FragmentBookBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_book

    val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateBegin(savedInstanceState: Bundle?) {
        mainViewModel.selectedBook.observe(this) {
            Glide.with(binding.root.context).load(it.image).into(binding.bookImage)
            binding.bookTitle.text = it.title
            binding.bookAuthor.text = it.author
            binding.bookPublisher.text = it.publisher
            binding.bookDescription.text = it.description
            mainViewModel.naver.getBookCatalog(it) { call, res, catalog, t ->
                if(res != null) {
                    if(res.isSuccessful) {
                        if(catalog != null) {
                            val contentTable = catalog.descriptions.contentTable
                            val list: MutableList<String> = BookContentParser.getBookContentTableList(contentTable)
                            binding.bookContents.apply {
                                adapter = BookContentAdapter(list)
                                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                            }
                        }
                    }
                }

            }
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