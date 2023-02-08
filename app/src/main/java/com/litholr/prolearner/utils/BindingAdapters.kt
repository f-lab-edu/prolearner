package com.litholr.prolearner.utils

import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

object BindingAdapters {
    @BindingAdapter("imageUrl")
    fun imageUrl(view: ImageView, url: String) {
        Log.d(this.javaClass.simpleName, "imageUrl($url)")
        Glide.with(view.context).load(url).into(view)
    }
}