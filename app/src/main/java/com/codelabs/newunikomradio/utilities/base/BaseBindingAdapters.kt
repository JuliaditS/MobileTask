package com.codelabs.newunikomradio.utilities.base

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("isGone")
fun bindIsGone(view: View, isGone: Boolean) {
    view.visibility = if (isGone) {
        View.GONE
    } else {
        View.VISIBLE
    }
}

@BindingAdapter("imageUrl")
fun setImageUrl(view: ImageView, imageUrl: String?) {
    if (!imageUrl.isNullOrEmpty()){
        Glide.with(view.context)
            .load(imageUrl)
            .dontAnimate()
            .into(view)
    }
}
