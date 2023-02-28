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
    // 더보기 검색 할 수 있게 (새로 추가 한 것은 위로 올릴 수 있도록- 저장한 시점도 저장 할 수 있게) 정렬기능
    // 읽은 percentage 정도 계산해서 sorting
    private fun loadSavedBookList() {
        lifecycleScope.launch {
            mainViewModel.getSavedBookInfos().observe(this@HomeFragment) {
                binding.savedBookList.apply {
                    adapter = SavedBookAdapter(it)
                    layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                }
            }
        }
    }

    inner class SavedBookAdapter(private val savedBookInfoList: List<SavedBookInfo>) : RecyclerView.Adapter<SavedBookViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):SavedBookViewHolder {
            return SavedBookViewHolder(CardviewSavedbookinfoBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
        override fun onBindViewHolder(holder: SavedBookViewHolder, position: Int) {
            holder.bindItem(savedBookInfoList[position])
        }
        override fun getItemCount(): Int = savedBookInfoList.size
    }

    inner class SavedBookViewHolder(private val savedBookViewBinding: CardviewSavedbookinfoBinding): RecyclerView.ViewHolder(savedBookViewBinding.root) {
        fun bindItem(item: SavedBookInfo) {
            savedBookViewBinding.savedBookInfo = item
            savedBookViewBinding.root.setOnClickListener {
                mainViewModel.updateBottomNavToBook(item.bookResult, false)
            }
            item.catalog?.let {
                CoroutineScope(Dispatchers.Main).launch {
                    val contentInfoList = mainViewModel.getContentInfoListByISBN(it.isbn)
                    val contentListlength = contentInfoList.size.toDouble()
                    val checkedListlength = contentInfoList.asSequence().count { it.isChecked }.toDouble()
                    Log.d(this.javaClass.simpleName, "${checkedListlength}/${contentListlength} = ${checkedListlength / contentListlength}")
                    val percent = ((checkedListlength / contentListlength) * 100).toInt()
                    savedBookViewBinding.progress.apply {
                        if(percent >= 100) {
                            setProgressDrawableColor(resources.getColor(R.color.readCompletedColor))
                        } else {
                            setProgressDrawableColor(resources.getColor(R.color.readingColor))
                        }
                        setCornerRadius(10f)
                        setProgressPercentage((checkedListlength / contentListlength) * 100, false)
                    }
                    savedBookViewBinding.percentage.text = "${percent}%"
                    savedBookViewBinding.percentage.apply {
                        if(percent >= 100) {
                            setTextColor(resources.getColor(R.color.readCompletedColor))
                        } else {
                            setTextColor(resources.getColor(R.color.readingColor))
                        }
                    }
                }
            }
        }
    }


}


