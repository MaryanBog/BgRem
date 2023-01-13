package com.bgrem.presentation.common.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class HorizontalItemDecoration(
    horizontalMargin: Int = 0,
    verticalMargin: Int = 0,
    private val firstStartMargin: Int = 0,
    private val lastEndMargin: Int = 0
) : RecyclerView.ItemDecoration() {

    private val halfVerticalMargin = verticalMargin / 2
    private val halfHorizontalMargin = horizontalMargin / 2

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view)
        if (position == RecyclerView.NO_POSITION) return

        val newRect = when (position) {
            0 -> Rect(
                firstStartMargin,
                halfVerticalMargin,
                halfHorizontalMargin,
                halfVerticalMargin
            )
            state.itemCount - 1 -> Rect(
                halfHorizontalMargin,
                halfVerticalMargin,
                lastEndMargin,
                halfVerticalMargin
            )
            else -> Rect(
                halfHorizontalMargin,
                halfVerticalMargin,
                halfHorizontalMargin,
                halfVerticalMargin
            )
        }

        outRect.set(newRect)
    }
}