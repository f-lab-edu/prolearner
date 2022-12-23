package com.litholr.prolearner.ui.base

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.litholr.prolearner.R
import com.litholr.prolearner.databinding.LayoutBaseBinding

class BaseLayout: ConstraintLayout {
    lateinit var layoutBaseBinding : LayoutBaseBinding

    constructor(context: Context) : this(context, null) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {
        init()
        getAttrs(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
        getAttrs(attrs, defStyleAttr)
    }

    fun init() {
//        val layoutInflateService = Context.LAYOUT_INFLATER_SERVICE
//        val layoutInflater = context.getSystemService(layoutInflateService) as LayoutInflater
//        val view = layoutInflater.inflate(R.layout.layout_base, this, false)
        layoutBaseBinding = LayoutBaseBinding.inflate(LayoutInflater.from(context))
        addView(layoutBaseBinding.rootLayout)
    }

    private fun getAttrs(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.BaseLayout)
        setTypeArray(typedArray)
    }

    // 만들어 놓은 attrs을 참조합니다.
    private fun getAttrs(attrs: AttributeSet?, defStyleAttr: Int) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.BaseLayout, defStyleAttr, 0)
        setTypeArray(typedArray)
    }

    private fun setTypeArray(typedArray: TypedArray) {
        typedArray.recycle()
    }
}