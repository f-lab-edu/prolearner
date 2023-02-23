package com.litholr.prolearner.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.litholr.prolearner.R
import com.litholr.prolearner.data.local.entity.SavedBookInfo
import com.litholr.prolearner.databinding.CardviewSavedbookinfoBinding
import com.litholr.prolearner.databinding.FragmentHomeBinding
import com.litholr.prolearner.ui.base.BaseFragment
import com.litholr.prolearner.ui.main.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_home

    val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateBegin(savedInstanceState: Bundle?) {
        loadSavedBookList()
    }

    private fun loadSavedBookList() {
        lifecycleScope.launch {
            mainViewModel.getSavedBookInfos().observe(this@HomeFragment) {
                binding.savedBookList.apply {
                    CoroutineScope(Dispatchers.Main).launch {
                        adapter = SavedBookAdapter(it)
                    }
                    layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                }
            }
        }
    }

    inner class SavedBookAdapter(val array: List<SavedBookInfo>) : RecyclerView.Adapter<SavedBookViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedBookViewHolder {
            return SavedBookViewHolder(CardviewSavedbookinfoBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
        override fun onBindViewHolder(holder: SavedBookViewHolder, position: Int) {
            holder.bindItem(array[position])
        }
        override fun getItemCount(): Int = array.size
    }
    inner class SavedBookViewHolder(private val savedBookViewBinding: CardviewSavedbookinfoBinding): RecyclerView.ViewHolder(savedBookViewBinding.root) {
        fun bindItem(item: SavedBookInfo) {
            item.catalog?.let {
                Glide.with(savedBookViewBinding.root.context).load(it.thumbnailUrl).into(savedBookViewBinding.bookImage)
                savedBookViewBinding.bookCategory.text = it.categoryName
                savedBookViewBinding.title.text = it.title
                savedBookViewBinding.author.text = it.authorList.joinToString(", ")
                savedBookViewBinding.publisher.text = it.publisher
                savedBookViewBinding.root.setOnClickListener {
                    mainViewModel.updateBottomNavToBook(item.bookResult, false)
                }
                CoroutineScope(Dispatchers.Main).launch {
                    val contentInfoList = mainViewModel.getContentInfoListByISBN(it.isbn)
                    val contentListlength = contentInfoList.size.toDouble()
                    val checkedListlength = contentInfoList.asSequence().count { it.isChecked }.toDouble()
                    Log.d(this.javaClass.simpleName, "${checkedListlength}/${contentListlength} = ${checkedListlength / contentListlength}")
                    val percent = ((checkedListlength / contentListlength) * 100).toInt()
                    savedBookViewBinding.progress.apply {
                        setCornerRadius(10f)
                        setProgressPercentage((checkedListlength / contentListlength) * 100, false)
                    }
                    savedBookViewBinding.percentage.text = "${percent}%"
                }
            }
        }
    }
}


