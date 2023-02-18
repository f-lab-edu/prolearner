package com.litholr.prolearner.ui.book

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ejjang2030.bookcontentparser.api.naver.BookCatalog
import com.litholr.prolearner.R
import com.litholr.prolearner.data.local.entity.SavedBookInfo
import com.litholr.prolearner.databinding.BookContentItemBinding
import com.litholr.prolearner.databinding.FragmentBookBinding
import com.litholr.prolearner.ui.base.BaseFragment
import com.litholr.prolearner.ui.main.MainViewModel
import com.litholr.prolearner.utils.CustomDatePicker
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList

class BookFragment: BaseFragment<FragmentBookBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_book

    val mainViewModel: MainViewModel by activityViewModels()

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreateBegin(savedInstanceState: Bundle?) {
        mainViewModel.selectedBook.observe(this) { bookResult ->
            GlobalScope.launch(Dispatchers.Default) {
                if(mainViewModel.isBookExisted(bookResult.isbn)) {
                    val savedBookInfo = mainViewModel.getSavedBookInfoByISBN(bookResult.isbn)
                    savedBookInfo.catalog?.let {
                        initUI(it, false)
                    }
                } else {
                    mainViewModel.naver.getBookCatalog(bookResult) { bresult, catalog, call, res, t ->
                        if (res?.isSuccessful == true) {
                            if (catalog == null) {
                                mainViewModel.showToastNullOfBookInfo()
                                return@getBookCatalog
                            } else {
                                mainViewModel.savedBookInfo = SavedBookInfo(
                                    isbn = bookResult.isbn,
                                    startDate = null,
                                    endDate = null,
                                    catalog,
                                    bookResult
                                )
                                initUI(catalog)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun initUI(catalog: BookCatalog, isFirst: Boolean = true) {
        CoroutineScope(Dispatchers.Main).launch {
            val contentLists = catalog.getBookContentTableList()
            if(contentLists == null) {
                mainViewModel.showToastNullOfBookContents()
                return@launch
            }
            binding.catalog = catalog
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
            val bookAdapter: BookContentAdapter
            if(isFirst) {
                binding.datePicker.visibility = View.GONE
                bookAdapter = BookContentAdapter(contentLists)
            } else {
                binding.datePicker.visibility = View.VISIBLE
                bookAdapter = BookContentAdapter(contentLists, false)
                initDatePickers()
            }
            binding.bookContents.apply {
                adapter = bookAdapter
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            }
        }
    }

    private fun initDatePickers() {
        binding.startDate.setOnClickListener {
            val datePicker = CustomDatePicker(requireActivity(), object: CustomDatePicker.ICustomDateListener {
                override fun onCancel() {
                }

                override fun onSet(
                    dialog: Dialog,
                    calendarSelected: Calendar,
                    dateSelected: Date,
                    year: Int,
                    monthFullName: String,
                    monthShortName: String,
                    monthNumber: Int,
                    day: Int,
                    weekDayFullName: String,
                    weekDayShortName: String
                ) {
                    binding.startDatePick.text = "$year.${monthNumber + 1}.$day"
                }

            }).apply {
                setDate(Calendar.getInstance())
                showDialog()
            }
        }
        binding.endDate.setOnClickListener {
            val datePicker = CustomDatePicker(requireActivity(), object: CustomDatePicker.ICustomDateListener {
                override fun onCancel() {
                }

                override fun onSet(
                    dialog: Dialog,
                    calendarSelected: Calendar,
                    dateSelected: Date,
                    year: Int,
                    monthFullName: String,
                    monthShortName: String,
                    monthNumber: Int,
                    day: Int,
                    weekDayFullName: String,
                    weekDayShortName: String
                ) {
                    binding.endDatePick.text = "$year.${monthNumber + 1}.$day"
                }

            }).apply {
                setDate(Calendar.getInstance())
                showDialog()
            }
        }
    }

    inner class BookContentAdapter(val array: List<String> = ArrayList(), val isFirst: Boolean = true) : RecyclerView.Adapter<BookContentViewHolder>() {

        val map = mutableMapOf<Int, Pair<String, Boolean>>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookContentViewHolder {
            return BookContentViewHolder(BookContentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }

        override fun onBindViewHolder(holder: BookContentViewHolder, position: Int) {
            holder.bindItem(array[position])
            map[position] = Pair(array[position], false)
            if(isFirst) {
                holder.binding().check.visibility = View.GONE
            } else {
                holder.binding().check.visibility = View.VISIBLE
                holder.binding().check.setOnClickListener {
                    it.isSelected = !it.isSelected
                }
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