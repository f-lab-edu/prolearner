package com.litholr.prolearner.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import api.naver.BookResult
import com.bumptech.glide.Glide
import com.litholr.prolearner.R
import com.litholr.prolearner.databinding.CardviewBookinfoBinding
import com.litholr.prolearner.databinding.FragmentSearchBinding
import com.litholr.prolearner.ui.base.BaseFragment
import com.litholr.prolearner.ui.main.MainActivity
import com.litholr.prolearner.ui.main.MainViewModel
import com.litholr.prolearner.utils.PLToast

class SearchFragment: BaseFragment<FragmentSearchBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_search

    val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateBegin(savedInstanceState: Bundle?) {
        mainViewModel.books.observe(viewLifecycleOwner) {
            Log.d(this.javaClass.simpleName, "onCreateBegin() ${it}")
            binding.bookList.apply {
                adapter = BookAdapter(it)
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                addOnScrollListener(object: RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        if(!recyclerView.canScrollVertically(1)) {
                            mainViewModel.page.postValue(mainViewModel.page.value?.plus(1))
                        }
                    }
                })
            }
        }
    }

    inner class BookAdapter(val array: ArrayList<BookResult> = ArrayList()) : RecyclerView.Adapter<BookViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
            return BookViewHolder(CardviewBookinfoBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }

        override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
            holder.bindItem(array[position])
        }

        override fun getItemCount(): Int = array.size
    }

    inner class BookViewHolder(private val bookViewBinding: CardviewBookinfoBinding): RecyclerView.ViewHolder(bookViewBinding.root) {

        fun bindItem(item: BookResult) {
            Glide.with(bookViewBinding.root.context).load(item.image).into(bookViewBinding.bookImage)
            bookViewBinding.title.text = item.title
            bookViewBinding.author.text = item.author
            bookViewBinding.publisher.text = item.publisher
            bookViewBinding.description.text = item.description
            bookViewBinding.root.setOnClickListener {
                mainViewModel.selectedBook.postValue(item)
                navigateTo(it, R.id.toBookFragment)
            }
        }
    }
}