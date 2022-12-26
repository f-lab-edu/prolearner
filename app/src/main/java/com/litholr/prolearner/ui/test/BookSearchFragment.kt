package com.litholr.prolearner.ui.test

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.litholr.prolearner.R
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

        binding.search.setOnClickListener {
            PLToast.makeToast(
                requireContext(),
                "query : ${binding.query.text} / display : ${binding.number.text} / page : ${binding.page.text}",
                Toast.LENGTH_SHORT)
            viewModel.searchBook(binding.query.text.toString(), binding.number.text.toString().toInt(), binding.page.text.toString().toInt())
            binding.result.text = viewModel.results.value
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }
}