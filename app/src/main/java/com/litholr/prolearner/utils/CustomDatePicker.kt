package com.litholr.prolearner.utils

import android.app.Activity
import android.app.Dialog
import android.content.res.Configuration
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.*
import androidx.core.content.ContextCompat
import com.litholr.prolearner.R
import java.text.SimpleDateFormat
import java.util.*

class CustomDatePicker(
    private val activity: Activity,
    customDateListener: ICustomDateListener
) : View.OnClickListener {
    private var datePicker: DatePicker? = null
    private var viewSwitcher: ViewSwitcher? = null

    private var btnSetDate: Button? = null
    private var btnSet: Button? = null
    private var btnCancel: Button? = null

    private var calendarDate: Calendar? = null

    private var iCustomDateListener: ICustomDateListener? = null

    private val dialog: Dialog

    private var isAutoDismiss = true

    private var maxDateInMillis: Long? = null
    private var minDateInMillis: Long? = null

    private val datePickerLayout: View
        get() {
            val linearMatchWrap = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            val linearWrapWrap = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            val frameMatchWrap = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )

            val buttonParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f)

            val linearMain = LinearLayout(activity)
            linearMain.layoutParams = linearMatchWrap
            linearMain.orientation = LinearLayout.VERTICAL
            linearMain.gravity = Gravity.CENTER



            val linearChild = LinearLayout(activity)
            linearChild.layoutParams = linearWrapWrap
            linearChild.orientation = LinearLayout.VERTICAL

            viewSwitcher = ViewSwitcher(activity)
            viewSwitcher!!.layoutParams = frameMatchWrap

            datePicker = DatePicker(activity)
            viewSwitcher!!.addView(datePicker)

            val linearBottom = LinearLayout(activity)
            linearMatchWrap.topMargin = 8
            linearBottom.layoutParams = linearMatchWrap

            btnSet = Button(activity)
            btnSet?.apply {
                layoutParams = buttonParams
                text = "선택"
                id = SET
                isAllCaps = false
                setTextColor(ContextCompat.getColor(context, R.color.colorBlack))
                background = context.getDrawable(R.color.colorTransparent)
                setOnClickListener(this@CustomDatePicker)
            }

            btnCancel = Button(activity)
            btnCancel?.apply {
                layoutParams = buttonParams
                text = "취소"
                id = CANCEL
                isAllCaps = false
                setTextColor(ContextCompat.getColor(context, R.color.colorBlack))
                background = context.getDrawable(R.color.colorTransparent)
                setOnClickListener(this@CustomDatePicker)
            }

            linearBottom.addView(btnCancel)
            linearBottom.addView(btnSet)
            linearChild.addView(viewSwitcher)
            linearChild.addView(linearBottom)

            linearMain.addView(linearChild)

            return linearMain
        }

    init {
        iCustomDateListener = customDateListener

        dialog = Dialog(activity)
        dialog.setOnDismissListener { resetData() }

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialogView = datePickerLayout
        dialog.setContentView(dialogView)
    }

    fun showDialog() {
        if (!dialog.isShowing) {
            if (calendarDate == null)
                calendarDate = Calendar.getInstance()

            updateDisplayedDate()

            dialog.show()
        }
    }

    private fun updateDisplayedDate() {
        datePicker?.updateDate(
            calendarDate!!.get(Calendar.YEAR),
            calendarDate!!.get(Calendar.MONTH),
            calendarDate!!.get(Calendar.DATE)
        )

        maxDateInMillis?.let {
            datePicker?.maxDate = maxDateInMillis as Long
        }
        minDateInMillis?.let {
            datePicker?.minDate = minDateInMillis as Long
        }
    }

    fun setMaxMinDisplayDate(minDate: Long? = null, maxDate: Long? = null) {
        minDate?.let {
            minDateInMillis = minDate
        }
        maxDate?.let {
            maxDateInMillis = maxDate
        }
    }

    fun setAutoDismiss(isAutoDismiss: Boolean) {
        this.isAutoDismiss = isAutoDismiss
    }

    fun dismissDialog() {
        if (!dialog.isShowing)
            dialog.dismiss()
    }

    fun setDate(calendar: Calendar?) {
        if (calendar != null)
            calendarDate = calendar
    }

    fun setDate(date: Date?) {
        if (date != null) {
            calendarDate = Calendar.getInstance()
            calendarDate!!.time = date
        }
    }

    fun setDate(year: Int, month: Int, day: Int) {
        if (month in 0..11 && day < 32 && day >= 0 && year > 100 && year < 3000) {
            calendarDate = Calendar.getInstance()
            calendarDate!!.set(year, month, day)
        }

    }

    interface ICustomDateListener {
        fun onSet(
            dialog: Dialog, calendarSelected: Calendar,
            dateSelected: Date, year: Int, monthFullName: String,
            monthShortName: String, monthNumber: Int, day: Int,
            weekDayFullName: String, weekDayShortName: String
        )

        fun onCancel()
    }

    override fun onClick(v: View) {
        when (v.id) {
            SET_DATE -> {
                btnSetDate!!.isEnabled = false
                btnSetDate!!.setTextColor(ContextCompat.getColor(activity, R.color.colorPrimary))

                if (viewSwitcher!!.currentView !== datePicker) {
                    viewSwitcher!!.showPrevious()
                }
            }

            SET_TIME -> {
                btnSetDate!!.isEnabled = true
                btnSetDate!!.setTextColor(ContextCompat.getColor(activity, R.color.colorBlack))

                if (viewSwitcher!!.currentView === datePicker) {
                    viewSwitcher!!.showNext()
                }
            }

            SET -> {
                if (iCustomDateListener != null) {
                    val month = datePicker!!.month
                    val year = datePicker!!.year
                    val day = datePicker!!.dayOfMonth

                    calendarDate!!.set(year, month, day)
                    iCustomDateListener!!.onSet(
                        dialog = dialog,
                        calendarSelected = calendarDate!!,
                        dateSelected = calendarDate!!.time,
                        year = calendarDate!!.get(Calendar.YEAR),
                        monthFullName = getMonthFullName(calendarDate!!.get(Calendar.MONTH)),
                        monthShortName = getMonthShortName(calendarDate!!.get(Calendar.MONTH)),
                        monthNumber = calendarDate!!.get(Calendar.MONTH),
                        day = calendarDate!!.get(Calendar.DAY_OF_MONTH),
                        weekDayFullName = getWeekDayFullName(calendarDate!!.get(Calendar.DAY_OF_WEEK)),
                        weekDayShortName = getWeekDayShortName(calendarDate!!.get(Calendar.DAY_OF_WEEK))
                    )
                }
                if (dialog.isShowing && isAutoDismiss)
                    dialog.dismiss()
            }

            CANCEL -> {
                if (iCustomDateListener != null)
                    iCustomDateListener!!.onCancel()
                if (dialog.isShowing)
                    dialog.dismiss()
            }
        }
    }

    private fun getMonthFullName(monthNumber: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MONTH, monthNumber)

        val simpleDateFormat = SimpleDateFormat("MMMM")
        simpleDateFormat.calendar = calendar

        return simpleDateFormat.format(calendar.time)
    }

    private fun getMonthShortName(monthNumber: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MONTH, monthNumber)

        val simpleDateFormat = SimpleDateFormat("MMM")
        simpleDateFormat.calendar = calendar

        return simpleDateFormat.format(calendar.time)
    }

    private fun getWeekDayFullName(weekDayNumber: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, weekDayNumber)

        val simpleDateFormat = SimpleDateFormat("EEEE")
        simpleDateFormat.calendar = calendar

        return simpleDateFormat.format(calendar.time)
    }

    private fun getWeekDayShortName(weekDayNumber: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, weekDayNumber)

        val simpleDateFormat = SimpleDateFormat("EE")
        simpleDateFormat.calendar = calendar

        return simpleDateFormat.format(calendar.time)
    }

    private fun getHourIn12Format(hour24: Int): Int {
        return when {
            hour24 == 0 -> 12
            hour24 <= 12 -> hour24
            else -> hour24 - 12
        }
    }

    private fun getAMPM(calendar: Calendar): String {
        return if (calendar.get(Calendar.AM_PM) == Calendar.AM)
            "am"
        else
            "pm"
    }

    private fun resetData() {
        calendarDate = null
    }

    companion object {
        /**
         * @param date       date in String
         * @param fromFormat format of your **date** eg: if your date is 2011-07-07
         * 09:09:09 then your format will be **yyyy-MM-dd hh:mm:ss**
         * @param toFormat   format to which you want to convert your **date** eg: if
         * required format is 31 July 2011 then the toFormat should be
         * **d MMMM yyyy**
         * @return formatted date
         */
        private const val SET_DATE = 100
        private const val SET_TIME = 101
        private const val SET = 102
        private const val CANCEL = 103

        fun convertDate(_date: String, fromFormat: String, toFormat: String): String {
            var date = _date
            try {
                var simpleDateFormat = SimpleDateFormat(fromFormat)
                val d = simpleDateFormat.parse(date)
                val calendar = Calendar.getInstance()
                calendar.time = d

                simpleDateFormat = SimpleDateFormat(toFormat)
                simpleDateFormat.calendar = calendar
                date = simpleDateFormat.format(calendar.time)

            } catch (e: Exception) {
                e.printStackTrace()
            }
            return date
        }

        fun pad(integerToPad: Int): String {
            return if (integerToPad >= 10 || integerToPad < 0)
                integerToPad.toString()
            else
                "0$integerToPad"
        }
    }

    private fun hideKeyboardInputInTimePicker(orientation: Int, timePicker: TimePicker) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    ((timePicker.getChildAt(0) as LinearLayout).getChildAt(4) as LinearLayout).getChildAt(
                        0
                    ).visibility = View.GONE
                } else {
                    (((timePicker.getChildAt(0) as LinearLayout).getChildAt(2) as LinearLayout).getChildAt(
                        2
                    ) as LinearLayout).getChildAt(0).visibility = View.GONE
                }
            } catch (ex: Exception) {
            }
        }
    }
}