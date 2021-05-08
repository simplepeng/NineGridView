package me.simple.view

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class NineGridView<VH : RecyclerView.ViewHolder> @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    var spanCount = 3

    var maxCount = Int.MAX_VALUE

    var childMargin = 0

    var adapter: RecyclerView.Adapter<VH>? = null
        set(value) {
            field = value
            addViews()
        }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (adapter == null) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            return
        }
        val displayCount = getDisplayCount()
        val lineCount = displayCount / spanCount + 1
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = width / spanCount * lineCount

        measureChildren(widthMeasureSpec, widthMeasureSpec)

        setMeasuredDimension(width, height)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        layoutChildren()
    }

    private fun layoutChildren() {
        val itemWidth = width / spanCount

        var left = 0
        var top = 0
        var right = 0
        var bottom = 0

        for (i in 0 until childCount) {
            val child = getChildAt(i)
            right = left + itemWidth
            bottom = top + itemWidth

            child.layout(left, top, right, bottom)

            if ((i + 1) % spanCount == 0) {//
                left = 0
                top = bottom
            } else {
                left = right
            }
        }
    }

    private fun addViews() {
        removeAllViewsInLayout()
        val displayCount = getDisplayCount()
        for (position in 0 until displayCount) {
            val itemViewType = adapter!!.getItemViewType(position)
            val holder = adapter!!.createViewHolder(this, itemViewType)
            val itemView = holder.itemView
            addViewInLayout(itemView, position, createChildLayoutParams(), true)
            adapter!!.bindViewHolder(holder, position)
        }
        requestLayout()
    }

    private fun createChildLayoutParams() =
        LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)

    private fun getDisplayCount(): Int {
        if (adapter == null) return 0
        return if (adapter!!.itemCount > maxCount) maxCount else adapter!!.itemCount
    }
}