package me.simple.view

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import kotlin.math.ceil

open class NineGridView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    //横向的item数量
    var spanCount = 3

    //最多显示的item数量
    var maxCount = 9

    //item间的间距
    var itemGap = 0

    //单个item时的适配策略
    var singleStrategy: Int = Strategy.USUAL

    //两个item时的适配策略
    var twoStrategy: Int = Strategy.USUAL

    //三个item时的适配策略
    var threeStrategy: Int = Strategy.USUAL

    //三个item时的适配策略
    var fourStrategy: Int = Strategy.USUAL

    //适配器
    var adapter: Adapter? = null
        set(value) {
            field = value
            addViews()
        }

    object Strategy {
        const val USUAL = 0//常规的
        const val FILL = 1//填充宽度的
        const val CUSTOM = 2//自定义
        const val BILI = 3//仿B站

        //额外Item是否显示
        const val SHOW = 0
        const val HIDE = 1
    }

    init {
        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.NineGridView)
            spanCount = typedArray.getInt(R.styleable.NineGridView_ngv_spanCount, spanCount)
            maxCount = typedArray.getInt(R.styleable.NineGridView_ngv_maxCount, maxCount)
            itemGap = typedArray.getDimension(R.styleable.NineGridView_ngv_itemGap, 2f).toInt()

            singleStrategy = typedArray.getInt(
                R.styleable.NineGridView_ngv_single_strategy,
                Strategy.USUAL
            )
            twoStrategy = typedArray.getInt(
                R.styleable.NineGridView_ngv_two_strategy,
                Strategy.USUAL
            )
            threeStrategy = typedArray.getInt(
                R.styleable.NineGridView_ngv_three_strategy,
                Strategy.USUAL
            )
            fourStrategy = typedArray.getInt(
                R.styleable.NineGridView_ngv_four_strategy,
                Strategy.USUAL
            )

            typedArray.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (adapter == null || adapter!!.getItemCount() == 0) {
            super.onMeasure(widthMeasureSpec, 0)
            return
        }

        //测量单个itemView的情况
//        if (singleStrategy == Strategy.USUAL && adapter.getItemCount() == 1) {
//            measureChildren(widthMeasureSpec, heightMeasureSpec)
//            val adaptHeightMeasureSpec =
//                MeasureSpec.makeMeasureSpec(getChildAt(0).measuredHeight, MeasureSpec.EXACTLY)
//            super.onMeasure(widthMeasureSpec, adaptHeightMeasureSpec)
//            return
//        }

//        measureChildren(widthMeasureSpec, heightMeasureSpec)
        when (adapter!!.getItemCount()) {
            1 -> {
                measureSingleItem(widthMeasureSpec, heightMeasureSpec)
            }
            2 -> {
                measureTwoItem(widthMeasureSpec)
            }
            3 -> {
                measureThreeItem(widthMeasureSpec)
            }
            4 -> {
                measureFourItem(widthMeasureSpec)
            }
            else -> {
                measureUsualItem(widthMeasureSpec)
            }
        }

    }

    //测量常规类型的Item
    private fun measureUsualItem(widthMeasureSpec: Int) {
        val lineCount = getLineCount()
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = width / spanCount * lineCount

        val itemSize = (width - (itemGap * (spanCount - 1))) / spanCount
        val childMeasureSpec = MeasureSpec.makeMeasureSpec(itemSize, MeasureSpec.EXACTLY)
//        measureChildren(widthMeasureSpec, widthMeasureSpec)
        for (i in 0 until childCount) {
            val itemView = getChildAt(i)
            itemView.measure(childMeasureSpec, childMeasureSpec)
        }

        setMeasuredDimension(width, height)
    }

    //
    private fun measureItemFill(
        widthMeasureSpec: Int,
        lineCount: Int
    ) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val itemSize = (width - itemGap) / 2
        val childMeasureSpec = MeasureSpec.makeMeasureSpec(itemSize, MeasureSpec.EXACTLY)
        measureChildren(childMeasureSpec, childMeasureSpec)
        val height = itemSize * lineCount + ((lineCount - 1) * itemGap)

        setMeasuredDimension(width, height)
    }

    //测量单个Item
    private fun measureSingleItem(
        widthMeasureSpec: Int,
        heightMeasureSpec: Int
    ) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        var height = MeasureSpec.getSize(heightMeasureSpec)
        when (singleStrategy) {
            Strategy.FILL -> {
                height = width
            }
            Strategy.CUSTOM -> {

            }
        }
        setMeasuredDimension(width, height)
    }

    //测量两个Item
    private fun measureTwoItem(widthMeasureSpec: Int) {
        when (twoStrategy) {
            Strategy.FILL -> {
                measureItemFill(widthMeasureSpec, 1)
            }
            else -> {
                measureUsualItem(widthMeasureSpec)
            }
        }
    }

    //测量三个Item
    private fun measureThreeItem(widthMeasureSpec: Int) {
        when (threeStrategy) {
            Strategy.FILL -> {
                measureItemFill(widthMeasureSpec, 2)
            }
            else -> {
                measureUsualItem(widthMeasureSpec)
            }
        }
    }


    private fun measureFourItem(widthMeasureSpec: Int) {
        when (fourStrategy) {
            Strategy.FILL -> {
                measureItemFill(widthMeasureSpec, 2)
            }
            else -> {
                measureUsualItem(widthMeasureSpec)
            }
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        layoutChildren()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (isInEditMode) {
            adapter = InEditModeAdapter(maxCount)
        }
    }

    private fun layoutChildren() {
        if (adapter == null) return
        val adapter = adapter!!

        when (childCount) {
            1 -> {
                layoutSingleItem()
            }
            2 -> {
                layoutTwoItem()
            }
            3 -> {
                layoutThreeItem()
            }
            4 -> {
                layoutFourItem()
            }
            else -> {
                layoutUsualItem2(spanCount)
            }
        }

        //绑定数据
        post {
            for (index in 0 until childCount) {
                val child = getChildAt(index)

                if (child.layoutParams is ItemViewLayoutParams) {
                    val lp = child.layoutParams as ItemViewLayoutParams
                    when (lp.type) {
                        ItemViewLayoutParams.TYPE_ITEM_VIEW -> {
                            adapter.onBindItemView(child, index)
                        }
                        ItemViewLayoutParams.TYPE_EXTRA_VIEW -> {
                            adapter.onBindExtraView(child, index)
                        }
                        ItemViewLayoutParams.TYPE_SINGLE_VIEW -> {
                            adapter.onBindSingleView(child, index)
                        }
                    }
                }
            }
        }
    }

    //布局一个item的情况
    private fun layoutSingleItem() {
        val singleView = getChildAt(0)
        when (singleStrategy) {
            Strategy.USUAL -> {
                val itemSize = width / spanCount
                singleView.layout(0, 0, itemSize, itemSize)
            }
            Strategy.FILL -> {
                singleView.layout(0, 0, width, width)
            }
            Strategy.CUSTOM -> {
                singleView.layout(0, 0, singleView.measuredWidth, measuredHeight)
            }
        }
    }

    private fun layoutTwoItem() {
        layoutUsualItem2(spanCount)
    }

    private fun layoutThreeItem() {
        when (threeStrategy) {
            Strategy.FILL, Strategy.BILI -> {
                layoutUsualItem2(2)
            }
            else -> {
                layoutUsualItem2(spanCount)
            }
        }

    }

    private fun layoutUsualItem2(skipLinePosition: Int) {

        var left = 0
        var top = 0
        var right = 0
        var bottom = 0

        val itemCount = getDisplayCount()
        for (i in 0 until itemCount) {
            val itemView = getChildAt(i)
            right = left + itemView.measuredWidth
            bottom = top + itemView.measuredHeight

            itemView.layout(left, top, right, bottom)

            if ((i + 1) % skipLinePosition == 0) {//
                left = 0
                top = bottom + itemGap
            } else {
                left = right + itemGap
            }
        }
    }

    private fun layoutFourItem() {
        var left = 0
        var top = 0
        var right = 0
        var bottom = 0

        when (fourStrategy) {
            Strategy.BILI -> {
                layoutUsualItem2(2)
            }
            Strategy.FILL -> {
                layoutUsualItem2(2)
            }
            else -> {
                layoutUsualItem2(spanCount)
            }
        }


    }

//    private fun layoutUsualItem() {
//        val adapter = adapter ?: return
//
//        var left = 0
//        var top = 0
//        var right = 0
//        var bottom = 0
//
//        val displayCount = getDisplayCount()
//        for (i in 0 until displayCount) {
//            val child = getChildAt(i)
//            right = left + child.measuredWidth
//            bottom = top + child.measuredWidth
//
//            child.layout(left, top, right, bottom)
//            if (isInEditMode) {
//                adapter.onBindItemView(child, i)
//            }
//
////            val skipPosition =
////                if (adapter.adaptFourItem() && displayCount == 4) 2 else spanCount
////            if ((i + 1) % skipPosition == 0) {//
////                left = 0
////                top = bottom + itemGap
////            } else {
////                left = right + itemGap
////            }
//        }
//    }

    //需要添加额外itemView的情况
//    private fun layoutExtraView() {
//        if (adapter == null) return
//        if (adapter!!.enableExtraView() && adapter!!.getItemCount() > maxCount) {
//            val extraView = getChildAt(childCount - 1)
//            right = width
//            left = right - extraView.measuredWidth
//            bottom = height
//            top = bottom - extraView.measuredWidth
//            extraView.layout(left, top, right, bottom)
//        }
//    }

    private fun addViews() {
        removeAllViewsInLayout()

        if (adapter == null || adapter!!.getItemCount() == 0) {
            requestLayout()
            return
        }

        val adapter = adapter ?: return
        val displayCount = getDisplayCount()
        var itemViewType = adapter.getItemViewType(0)

        //要适配单个View的情况
//        val singleView = adapter.onCreateSingleView(this, itemViewType)
//        if (singleView != null && adapter.getItemCount() == 1) {
//            addViewInLayout(
//                singleView,
//                0,
//                createSingleViewLayoutParams(singleView),
//                true
//            )
//            requestLayout()
//            return
//        }

        //默认itemView的情况
        for (position in 0 until displayCount) {
            itemViewType = adapter.getItemViewType(position)
            val itemView = adapter.onCreateItemView(this, itemViewType)
            val itemViewLayoutParams = createItemViewLayoutParams(
                ItemViewLayoutParams.TYPE_ITEM_VIEW
            )
            addViewInLayout(itemView, position, itemViewLayoutParams, true)
        }

        //添加额外的itemView
//        itemViewType = adapter.getItemViewType(displayCount)
//        val extraView = adapter.onCreateExtraView(this, itemViewType)
//        if (adapter.enableExtraView() && extraView != null && adapter.getItemCount() > maxCount) {
//            addViewInLayout(
//                extraView,
//                displayCount,
//                createItemViewLayoutParams(ItemViewLayoutParams.TYPE_EXTRA_VIEW),
//                true
//            )
//        }

        //
        requestLayout()
    }

    /**
     * 创建itemView的LayoutParams
     */
    private fun createSingleViewLayoutParams(singleView: View): LayoutParams {
        return if (singleStrategy == Strategy.CUSTOM) {
            singleView.layoutParams
        } else {
            ItemViewLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT).apply {
                type = ItemViewLayoutParams.TYPE_SINGLE_VIEW
            }
        }
    }

    /**
     * 创建itemView的LayoutParams
     */
    private fun createItemViewLayoutParams(type: Int): LayoutParams {
        val itemViewLayoutParams =
            ItemViewLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        itemViewLayoutParams.type = type
        return itemViewLayoutParams
    }

    //
    internal class ItemViewLayoutParams(
        width: Int,
        height: Int
    ) : ViewGroup.MarginLayoutParams(width, height) {

        companion object {
            const val TYPE_SINGLE_VIEW = 1
            const val TYPE_ITEM_VIEW = 2
            const val TYPE_EXTRA_VIEW = 3
        }

        var type: Int = TYPE_ITEM_VIEW
    }

    //真实要显示的itemCount
    private fun getDisplayCount(): Int {
        if (adapter == null) return 0
        return if (adapter!!.getItemCount() > maxCount) maxCount else adapter!!.getItemCount()
    }

    //获取行数
    private fun getLineCount(): Int {
        return ceil(getDisplayCount().toDouble() / spanCount).toInt()
    }

    abstract class Adapter {

        // 返回总的item数量
        abstract fun getItemCount(): Int

        //分发各种不同itemType，类似RecyclerView
        fun getItemViewType(position: Int) = 0

        //适配4个itemView的样式-类似哔哩哔哩
//        open fun adaptFourItem(): Boolean = false

        //默认的ItemView样式
        abstract fun onCreateItemView(parent: ViewGroup, viewType: Int): View

        abstract fun onBindItemView(itemView: View, position: Int)

        //是否适配单个ItemView的样式
//        @Deprecated("")
//        open fun adaptSingleView(): Boolean = false

        open fun onCreateSingleView(parent: ViewGroup, viewType: Int): View? = null

        open fun onBindSingleView(singleView: View, position: Int) {

        }

        //是否适配额外的View的样式
//        open fun enableExtraView(): Boolean = false

        open fun onCreateExtraView(parent: ViewGroup, viewType: Int): View? = null

        open fun onBindExtraView(extraView: View, position: Int) {

        }
    }
}