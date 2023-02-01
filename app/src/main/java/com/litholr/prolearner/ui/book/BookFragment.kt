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
                if((res != null) && res.isSuccessful) {
                    if(catalog != null) {
                        Glide.with(binding.root.context).load(it.image).into(binding.bookImage)
                        binding.bookTitle.text = catalog.title
                        if(catalog.subtitle != null) {
                            binding.bookSubtitle.visibility = View.VISIBLE
                            binding.bookSubtitle.text = catalog.subtitle
                        } else {
                            binding.bookSubtitle.visibility = View.GONE
                        }
                        binding.infoAuthor.apply {
                            title.text = "저자"
                            text.text = catalog.authorList.joinToString(", ")
                        }
                        binding.infoPublisher.apply {
                            title.text = "출판사"
                            text.text = catalog.publisher
                        }
                        binding.infoResourceFrom.apply {
                            title.text = "정보제공"
                            text.text = catalog.contentsSourceMallName ?: "미확인"
                        }

                        binding.expandTextView.setOnExpandStateChangeListener { textView, isExpanded -> }
                        binding.expandTextView.text = catalog.description
                        binding.expandableText.text = catalog.description
                        val contentTable = catalog.contentsHtml
                        val contentLists = catalog.getBookContentTableList()
                        if(contentLists != null) {
                            viewPager2Adapter.addFragment(ContentsFragment(contentLists))
                            viewPager2Adapter.addFragment(GoalsFragment())
                        } else {
                            mainViewModel.plToastListener?.printToast("목차정보가 없습니다.")
                        }
                    } else {
                        mainViewModel.plToastListener?.printToast("책의 정보를 불러오지 못했습니다.")
                        mainViewModel.bottomNav.value = MainViewModel.BottomNav.SEARCH
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
}