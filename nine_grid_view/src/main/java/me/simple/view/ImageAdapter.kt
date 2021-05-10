package me.simple.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide

open class ImageAdapter(
    private val images: List<String>
) : NineGridView.Adapter() {

    override fun getItemCount() = images.size

    override fun onCreateItemView(parent: ViewGroup, viewType: Int): View {
        return LayoutInflater.from(parent.context).inflate(R.layout.layout_ngv_image, parent, false)
    }

    override fun onBindItemView(itemView: View, position: Int) {
        Glide.with(itemView)
            .load(images[position])
            .into(itemView as ImageView)
    }
}