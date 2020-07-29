package com.codelabs.newunikomradio.utilities.helper.recyclerviewhelper.itemdecoration

import android.content.Context
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class RecyclerviewItemGridTwoHorizontalDecoration(private val context: Context, private val spacing: Float) :
    RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val metrics = context.resources.displayMetrics
        val mPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, spacing, metrics).toInt()
        if (parent.getChildAdapterPosition(view) % 2 == 0) {
            outRect.left = mPadding
            outRect.right = mPadding /2
        } else {
            outRect.right = mPadding
            outRect.left = mPadding /2
        }
    }
}