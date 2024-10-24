package com.example.coolshop.main.ui

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

internal class ItemOffsetDecoration(private val mItemOffset:Int): RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.set(mItemOffset,mItemOffset,mItemOffset,mItemOffset)
    }
}
