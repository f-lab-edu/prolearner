package com.litholr.prolearner.ui.book

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.ejjang2030.bookcontentparser.BookContentParser
import com.google.android.material.tabs.TabLayoutMediator
import com.litholr.prolearner.R
import com.litholr.prolearner.databinding.BookContentItemBinding
import com.litholr.prolearner.databinding.FragmentBookBinding
import com.litholr.prolearner.ui.base.BaseFragment
import com.litholr.prolearner.ui.main.MainViewModel
import com.litholr.prolearner.utils.PLToast

class BookFragment: BaseFragment<FragmentBookBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_book

    val mainViewModel: MainViewModel by activityViewModels()

    lateinit var viewPager2Adapter: ViewPager2Adapter

    override fun onCreateBegin(savedInstanceState: Bundle?) {
        initViewPage()
        mainViewModel.selectedBook.observe(this) {
            mainViewModel.naver.getBookCatalog(it) { bresult, catalog, call, res, t ->
                if(res != null) {
                    if(res.isSuccessful) {
                        if(catalog != null) {
                            Glide.with(binding.root.context).load(it.image).into(binding.bookImage)
                            binding.bookTitle.text = catalog.title
                            if(catalog.subtitle != null) {
                                binding.bookSubtitle.visibility = View.VISIBLE
                                binding.bookSubtitle.text = catalog.subtitle
                            } else {
                                binding.bookSubtitle.visibility = View.GONE
                            }
                            binding.bookAuthor.text = catalog.authorList.joinToString(", ")
                            binding.bookPublisher.text = it.publisher

                            binding.expandTextView.setOnExpandStateChangeListener { textView, isExpanded -> }
                            binding.expandTextView.text = catalog.description
                            binding.expandableText.text = catalog.description
                            val contentTable = catalog.contentsHtml
                            val list: MutableList<String> = BookContentParser.getBookContentTableList(contentTable)
                            val contentLists = catalog.getBookContentTableList()
                            viewPager2Adapter.addFragment(ContentsFragment(contentLists))
                            viewPager2Adapter.addFragment(GoalsFragment())
                        }
                    }
                }
            }
        }
    }

    private fun initViewPage() {
        viewPager2Adapter = ViewPager2Adapter(this)
        binding.viewpagerMain.apply {
            adapter = viewPager2Adapter
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                }
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                }
            })
        }

        TabLayoutMediator(binding.tabNavigationView, binding.viewpagerMain) { tab, position ->
            when(position) {
                0 -> tab.text = "학습내용"
                1 -> tab.text = "목표설정"
            }
        }.attach()
    }

    inner class ViewPager2Adapter(fragment: Fragment): FragmentStateAdapter(fragment) {
        var fragments: ArrayList<Fragment> = ArrayList()

        override fun getItemCount(): Int {
            return fragments.size
        }

        override fun createFragment(position: Int): Fragment {
            return fragments[position]
        }

        fun addFragment(fragment: Fragment) {
            fragments.add(fragment)
            notifyItemInserted(fragments.size - 1)
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