package me.simple.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

open class ImageAdapter<T>(
    private val items: List<T>?,
    private val onBindView: ((imageView: ImageView, item: T, position: Int) -> Unit)?,
) : NineGridView.Adapter() {

    var onItemViewClick: ((item: T, position: Int) -> Unit)? = null
    var onExtraViewClick: ((position: Int) -> Unit)? = null

    //
    override fun getItemCount() = items?.size ?: 0

    //
    override fun onCreateItemView(parent: ViewGroup, viewType: Int): View {
        return LayoutInflater.from(parent.context).inflate(R.layout.ngv_item_image, parent, false)
    }

    override fun onBindItemView(itemView: View, position: Int) {
        if (items.isNullOrEmpty()) return
        val item = items[position] ?: return
        itemView.setOnClickListener {
            onItemViewClick?.invoke(item, position)
        }
        this.onBindView?.invoke(itemView as ImageView, item, position)
    }

    //
    override fun onCreateSingleView(parent: ViewGroup, viewType: Int): View? {
        return LayoutInflater.from(parent.context).inflate(R.layout.ngv_item_single, parent, false)
    }

    override fun onBindSingleView(singleView: View, position: Int) {
        if (items.isNullOrEmpty()) return
        val item = items[position] ?: return
        singleView.setOnClickListener {
            onItemViewClick?.invoke(item, position)
        }
        this.onBindView?.invoke(singleView as ImageView, item, position)
    }

    //
    override fun onCreateExtraView(parent: ViewGroup, viewType: Int): View? {
        return LayoutInflater.from(parent.context).inflate(R.layout.ngv_item_extra, parent, false)
    }

    override fun onBindExtraView(extraView: View, position: Int) {
        if (items.isNullOrEmpty()) return
        val tvExtra = extraView.findViewById<TextView>(R.id.tvExtra)
        val extraCount = items.size - position
        tvExtra.text = String.format("+%s", extraCount)
        extraView.setOnClickListener {
            onExtraViewClick?.invoke(position)
        }
    }
}