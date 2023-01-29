package com.litholr.prolearner.ui.book

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.litholr.prolearner.R
import com.litholr.prolearner.databinding.BookContentItemBinding
import com.litholr.prolearner.databinding.FragmentContentsBinding
import com.litholr.prolearner.databinding.FragmentGoalsBinding
import com.litholr.prolearner.ui.base.BaseFragment
import com.litholr.prolearner.utils.CustomDatePicker
import java.util.*

class GoalsFragment(): BaseFragment<FragmentGoalsBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_goals

    override fun onCreateBegin(savedInstanceState: Bundle?) {
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
}