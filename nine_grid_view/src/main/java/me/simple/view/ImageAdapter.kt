package me.simple.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

open class ImageAdapter<T>(
    private val items: List<T>,
    private val onBindView: (imageView: ImageView, item: T, position: Int) -> Unit,
) : NineGridView.Adapter() {

    var onItemViewClick: ((item: T, position: Int) -> Unit)? = null
    var onExtraViewClick: ((position: Int) -> Unit)? = null

    //
    override fun getItemCount() = items.size

    //是否适配4个ImageView
    override fun adaptFourItem() = true

    //
    override fun onCreateItemView(parent: ViewGroup, viewType: Int): View {
        return LayoutInflater.from(parent.context).inflate(R.layout.ngv_item_image, parent, false)
    }

    override fun onBindItemView(itemView: View, position: Int) {
        val item = items[position]
        itemView.setOnClickListener {
            onItemViewClick?.invoke(item, position)
        }
        this.onBindView(itemView as ImageView, item, position)
    }

    //适配单个ImageView
    override fun adaptSingleView() = true

    override fun onCreateSingleView(parent: ViewGroup, viewType: Int): View? {
        return LayoutInflater.from(parent.context).inflate(R.layout.ngv_item_single, parent, false)
    }

    override fun onBindSingleView(singleView: View, position: Int) {
        val item = items[position]
        singleView.setOnClickListener {
            onItemViewClick?.invoke(item, position)
        }
        this.onBindView(singleView as ImageView, item, position)
    }

    //是否适配显示额外的
    override fun enableExtraView() = true

    override fun onCreateExtraView(parent: ViewGroup, viewType: Int): View? {
        return LayoutInflater.from(parent.context).inflate(R.layout.ngv_item_extra, parent, false)
    }

    override fun onBindExtraView(extraView: View, position: Int) {
        val tvExtra = extraView.findViewById<TextView>(R.id.tvExtra)
        val extraCount = items.size - position
        tvExtra.text = String.format("+%s", extraCount)
        extraView.setOnClickListener {
            onExtraViewClick?.invoke(position)
        }
    }
}