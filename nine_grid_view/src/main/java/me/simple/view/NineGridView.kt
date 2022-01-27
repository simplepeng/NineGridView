package me.simple.view

import android.content.Context
import android.util.AttributeSet
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

    //额外布局是否显示
    var extraStrategy: Int = Strategy.SHOW

    //适配器
    var adapter: Adapter? = null
        set(value) {
            field = value
            addViews()
        }

    //适配策略模式
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
            extraStrategy = typedArray.getInt(
                R.styleable.NineGridView_ngv_extra_strategy,
                Strategy.SHOW
            )

            typedArray.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (adapter == null || adapter?.getItemCount() == 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            return
        }

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
                measureItem(widthMeasureSpec, spanCount, getLineCount())
            }
        }

    }

    //获取item的大小
    private fun getItemSize(
        widthMeasureSpec: Int,
        itemCount: Int = this.spanCount
    ) = (MeasureSpec.getSize(widthMeasureSpec) - (itemGap * (itemCount - 1))) / itemCount

    //测量item
    private fun measureItem(
        widthMeasureSpec: Int,
        itemCount: Int,
        lineCount: Int
    ) {
        val itemSize = getItemSize(widthMeasureSpec, itemCount)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = itemSize * lineCount + ((lineCount - 1) * itemGap)
        val childMeasureSpec = MeasureSpec.makeMeasureSpec(itemSize, MeasureSpec.EXACTLY)
        measureChildren(childMeasureSpec, childMeasureSpec)
        setMeasuredDimension(width, height)
    }

    //测量单个Item
    private fun measureSingleItem(
        widthMeasureSpec: Int,
        heightMeasureSpec: Int
    ) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = when (singleStrategy) {
            Strategy.FILL -> {
                measureChildren(widthMeasureSpec, widthMeasureSpec)
                width
            }
            Strategy.CUSTOM -> {
                measureChildren(widthMeasureSpec, heightMeasureSpec)
                getChildAt(0).measuredHeight
            }
            else -> {
                val itemsSize = getItemSize(widthMeasureSpec, spanCount)
                val itemMeasureSpec = MeasureSpec.makeMeasureSpec(itemsSize, MeasureSpec.EXACTLY)
                measureChildren(itemMeasureSpec, itemMeasureSpec)
                itemsSize
            }
        }
        setMeasuredDimension(width, height)
    }

    //测量两个Item
    private fun measureTwoItem(widthMeasureSpec: Int) {
        when (twoStrategy) {
            Strategy.FILL -> {
                measureItem(widthMeasureSpec, 2, 1)
            }
            else -> {
                measureItem(widthMeasureSpec, spanCount, 1)
            }
        }
    }

    //测量三个Item
    private fun measureThreeItem(widthMeasureSpec: Int) {
        when (threeStrategy) {
            Strategy.FILL -> {
                measureItem(widthMeasureSpec, spanCount, 1)
            }
            Strategy.BILI -> {
                measureItem(widthMeasureSpec, spanCount, 2)
            }
            else -> {//usual
                measureItem(widthMeasureSpec, spanCount, 1)
            }
        }
    }

    //测量四个Item
    private fun measureFourItem(widthMeasureSpec: Int) {
        when (fourStrategy) {
            Strategy.FILL -> {
                measureItem(widthMeasureSpec, 2, 2)
            }
            Strategy.BILI -> {
                measureItem(widthMeasureSpec, spanCount, 2)
            }
            else -> {//usual
                measureItem(widthMeasureSpec, spanCount, getLineCount())
            }
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        layoutChildren()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        //布局预览模式
        if (isInEditMode) {
            adapter = InEditModeAdapter(maxCount)
        }
    }

    //绑定数据
    private fun performBind() {
        if (adapter == null) return
        post {
            for (index in 0 until childCount) {
                val child = getChildAt(index)

                if (child.layoutParams is ItemViewLayoutParams) {
                    val lp = child.layoutParams as ItemViewLayoutParams
                    when (lp.type) {
                        ItemViewLayoutParams.TYPE_ITEM_VIEW -> {
                            adapter!!.onBindItemView(child, index)
                        }
                        ItemViewLayoutParams.TYPE_EXTRA_VIEW -> {
                            adapter!!.onBindExtraView(child, index)
                        }
                        ItemViewLayoutParams.TYPE_SINGLE_VIEW -> {
                            adapter!!.onBindSingleView(child, index)
                        }
                    }
                }
            }
        }
    }

    //
    private fun layoutChildren() {
        if (adapter == null) return

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
                layoutItem(spanCount)
                layoutExtraView()
            }
        }

        performBind()
    }

    //布局item
    private fun layoutItem(skipLinePosition: Int) {
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

            if ((i + 1) % skipLinePosition == 0) {//换行
                left = 0
                top = bottom + itemGap
            } else {
                left = right + itemGap
            }
        }
    }

    //布局一个item的情况
    private fun layoutSingleItem() {
        val singleView = getChildAt(0)
        singleView.layout(0, 0, singleView.measuredWidth, singleView.measuredHeight)
    }

    //布局两个item的情况
    private fun layoutTwoItem() {
        layoutItem(spanCount)
    }

    //布局三个item的情况
    private fun layoutThreeItem() {
        when (threeStrategy) {
            Strategy.BILI -> {
                layoutItem(2)
            }
            else -> {
                layoutItem(spanCount)
            }
        }
    }

    //布局四个item的情况
    private fun layoutFourItem() {
        when (fourStrategy) {
            Strategy.BILI, Strategy.FILL -> {
                layoutItem(2)
            }
            else -> {
                layoutItem(spanCount)
            }
        }
    }

    //需要添加额外itemView的情况
    private fun layoutExtraView() {
        if (adapter == null) return
        if (extraStrategy == Strategy.SHOW && adapter!!.getItemCount() > maxCount) {
            val extraView = getChildAt(childCount - 1)
            val lastView = getChildAt(childCount - 2)

            val left = lastView.left
            val top = lastView.top
            val right = lastView.right
            val bottom = lastView.bottom

            extraView.layout(left, top, right, bottom)
        }
    }

    //添加views
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
        val singleView = adapter.onCreateSingleView(this, itemViewType)
        if (singleView != null && adapter.getItemCount() == 1) {
            val singleViewLayoutParams = createSingleViewLayoutParams(singleView)
            addViewInLayout(singleView, 0, singleViewLayoutParams, true)
            requestLayout()
            return
        }

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
        if (adapter.getItemCount() > maxCount) {
            itemViewType = adapter.getItemViewType(displayCount)
            val extraView = adapter.onCreateExtraView(this, itemViewType)
            if (extraStrategy == Strategy.SHOW && extraView != null) {
                val layoutParams = createItemViewLayoutParams(ItemViewLayoutParams.TYPE_EXTRA_VIEW)
                addViewInLayout(extraView, displayCount, layoutParams, true)
            }
        }

        //
        requestLayout()
    }

    /**
     * 创建itemView的LayoutParams
     */
    private fun createSingleViewLayoutParams(singleView: View): LayoutParams {
        val layoutParams = ItemViewLayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        )
        layoutParams.type = ItemViewLayoutParams.TYPE_SINGLE_VIEW
        if (singleStrategy == Strategy.CUSTOM) {
            layoutParams.width = singleView.layoutParams.width
            layoutParams.height = singleView.layoutParams.height
        }
        return layoutParams
    }

    /**
     * 创建itemView的LayoutParams
     */
    private fun createItemViewLayoutParams(type: Int): LayoutParams {
        val itemViewLayoutParams = ItemViewLayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        )
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

    //适配器
    abstract class Adapter {

        // 返回总的item数量
        abstract fun getItemCount(): Int

        //分发各种不同itemType，类似RecyclerView
        open fun getItemViewType(position: Int) = 0

        //默认的ItemView样式
        abstract fun onCreateItemView(parent: ViewGroup, viewType: Int): View

        abstract fun onBindItemView(itemView: View, position: Int)

        //自定义单个item样式
        open fun onCreateSingleView(parent: ViewGroup, viewType: Int): View? = null

        open fun onBindSingleView(singleView: View, position: Int) {

        }

        //自定义额外item样式
        open fun onCreateExtraView(parent: ViewGroup, viewType: Int): View? = null

        open fun onBindExtraView(extraView: View, position: Int) {

        }
    }
}