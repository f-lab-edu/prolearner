package com.litholr.prolearner.ui.test

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import api.naver.BookResult
import com.bumptech.glide.Glide
import com.litholr.prolearner.R
import com.litholr.prolearner.databinding.CardviewBookinfoBinding
import com.litholr.prolearner.databinding.FragmentBookSearchBinding
import com.litholr.prolearner.utils.PLToast

class BookSearchFragment : Fragment() {

    companion object {
        fun newInstance() = BookSearchFragment()
    }

    private lateinit var viewModel: BookSearchViewModel
    private lateinit var binding: FragmentBookSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this).get(BookSearchViewModel::class.java)
        binding = FragmentBookSearchBinding.inflate(LayoutInflater.from(requireContext()))

        viewModel.results.observe(viewLifecycleOwner) {
            Log.d(this.javaClass.simpleName, "viewModel.results changed to $it")
        }
        viewModel.books.observe(viewLifecycleOwner) {
            binding.list.apply {
                adapter = BookAdapter(it)
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            }
        }

        binding.search.setOnClickListener {
            PLToast.makeToast(
                requireContext(),
                "query : ${binding.query.text} / display : ${binding.number.text} / page : ${binding.page.text}",
                Toast.LENGTH_SHORT)
            viewModel.searchBook(binding.query.text.toString(), binding.number.text.toString().toInt(), binding.page.text.toString().toInt())
        }


        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    class BookAdapter(val init: ArrayList<BookResult> = ArrayList()) : RecyclerView.Adapter<BookViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
            return BookViewHolder(CardviewBookinfoBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }

        override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
            holder.apply {
                bindItem(init[position])
            }
        }

        override fun getItemCount(): Int = init.size

    }

    class BookViewHolder(private val bookViewBinding: CardviewBookinfoBinding): RecyclerView.ViewHolder(bookViewBinding.root) {
        fun bindItem(item: BookResult) {
            Glide.with(bookViewBinding.root.context).load(item.image).into(bookViewBinding.bookImage)
            bookViewBinding.title.text = item.title
            bookViewBinding.author.text = item.author
            bookViewBinding.publisher.text = item.publisher
            bookViewBinding.description.text = item.description
        }
    }
}