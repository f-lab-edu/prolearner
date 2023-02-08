package com.litholr.prolearner.ui.search

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import api.naver.BookResult
import com.bumptech.glide.Glide
import com.github.pwittchen.infinitescroll.library.InfiniteScrollListener
import com.litholr.prolearner.R
import com.litholr.prolearner.databinding.CardviewBookinfoBinding
import com.litholr.prolearner.databinding.FragmentSearchBinding
import com.litholr.prolearner.ui.base.BaseFragment
import com.litholr.prolearner.ui.main.MainViewModel

class SearchFragment: BaseFragment<FragmentSearchBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_search

    val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateBegin(savedInstanceState: Bundle?) {
        binding.bookList.apply {
            adapter = BookAdapter(mainViewModel.books.value!!)
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            addOnScrollListener(object: RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if(!recyclerView.canScrollVertically(1)) {
                        showLoading()
                        mainViewModel.updateScrollPage()
                        recyclerView.scrollToPosition((layoutManager as LinearLayoutManager).findLastVisibleItemPosition())
                    }
                }
            })
        }
        mainViewModel.books.observe(this) {
            binding.bookList.adapter = BookAdapter(mainViewModel.books.value!!)
        }
    }

    private fun showLoading() {
        object: AsyncTask<Unit, Unit, Unit>() {
            override fun onPreExecute() {
                binding.progressBar.visibility = View.VISIBLE
            }

            override fun doInBackground(vararg p0: Unit?) {
                try {
                    Thread.sleep(1500)
                } catch(e: InterruptedException) {
                    Log.e(this.javaClass.simpleName, e.message.toString())
                }
            }

            override fun onPostExecute(result: Unit?) {
                binding.progressBar.visibility = View.GONE
            }
        }.execute()
    }

    inner class BookAdapter(val array: ArrayList<BookResult> = ArrayList()) : RecyclerView.Adapter<BookViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
            return BookViewHolder(CardviewBookinfoBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
        override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
            holder.bindItem(array[position])
        }
        override fun getItemCount(): Int = array.size
        fun getItems(): ArrayList<BookResult> {
            return array
        }
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
                mainViewModel.updateBottomNavToBook()
            }
        }
    }
}