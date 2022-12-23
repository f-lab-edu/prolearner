package com.litholr.prolearner.utils

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.litholr.prolearner.R
import com.litholr.prolearner.databinding.LayoutToastBinding

object PLToast {
    fun makeToast(context: Context, message: CharSequence, duration: Int) {
        context.mainLooper.thread.also {
            val toast = Toast(context)
            toast.duration = duration
            val binding: LayoutToastBinding = LayoutToastBinding.inflate(LayoutInflater.from(context))
            binding.message.text = message
            binding.message.setTextColor(context.getResources().getColor(R.color.purple_200))
            toast.view = binding.root
            return setToast(toast).show()
        }
    }

    private fun setToast(toast: Toast): Toast {
        toast
        return toast
    }
}