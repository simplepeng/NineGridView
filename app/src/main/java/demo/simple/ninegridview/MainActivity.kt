package demo.simple.ninegridview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.stfalcon.imageviewer.StfalconImageViewer
import demo.simple.ninegridview.databinding.ActivityMainBinding
import me.simple.loadmoreadapter.LoadMoreAdapter
import me.simple.view.ImageAdapter
import me.simple.view.NineGridView
import java.util.*

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(this.layoutInflater) }

    private val mImages by lazy {
        mutableListOf(
            "https://img2.baidu.com/it/u=111010057,1963484761&fm=26&fmt=auto",
            "https://img0.baidu.com/it/u=680204813,2408273793&fm=26&fmt=auto",
            "https://img0.baidu.com/it/u=4198545283,538361922&fm=26&fmt=auto",
            "https://img2.baidu.com/it/u=1042301913,584297321&fm=26&fmt=auto",
            "https://img0.baidu.com/it/u=3329176626,3830333762&fm=26&fmt=auto",
            "https://img1.baidu.com/it/u=2199515026,1341231739&fm=26&fmt=auto",
            "https://img2.baidu.com/it/u=1094955877,138168367&fm=26&fmt=auto",
            "https://img2.baidu.com/it/u=4162224702,2932016509&fm=253&fmt=auto",
            "https://img2.baidu.com/it/u=1273165577,938991422&fm=253&fmt=auto",
            "https://img0.baidu.com/it/u=1528947100,1813241635&fm=253&fmt=auto",
            "https://img0.baidu.com/it/u=613366496,3047981989&fm=253&fmt=auto",
            "https://img1.baidu.com/it/u=2665794537,3782072232&fm=26&fmt=auto",
            "https://img2.baidu.com/it/u=1502110763,2971686696&fm=253&fmt=auto",
            "https://img1.baidu.com/it/u=2270371530,4039976270&fm=253&fmt=auto",
            "https://img1.baidu.com/it/u=3895358213,633254202&fm=253&fmt=auto&",
            "https://img2.baidu.com/it/u=4042351128,2955686081&fm=253&fmt=auto",
        )
    }

    private val mTexts by lazy {
        mutableListOf(
            "首先把自己变成想要的样子， 然后遇一个无需取悦的人。 友情爱情或者生活都一样， 安全感都得自己给自己。",
            "春有百花秋有月，夏有凉风冬有雪。 若无闲事挂心头，便是人间好时节。",
            "你怎么舍得， 让一个满眼都是你的人， 扛着所有负面情绪， 一次又一次逼着自己放下你， 带着失落从而选择离开。",
            "心里藏着小星星，生活才能亮晶晶。 ",
            "愿你一生有山可靠 有树可栖 与心爱之人 春赏花 夏纳凉 秋登山 冬扫雪 ",
            "爱就是，我以为我要变得足够好才能遇见你，却发现原来是遇见了你，我才变成一个最好的我。",
            "我们曾如此期待他人的认同，到最后才知道，世界是自己的，与他人毫无关系。",
            "后来我渐渐明白 人生就是不断放下的过程 遗憾的是 很多时候我们都没有好好道别",
            "还是该开阔自己的视野 去减肥、去打扮、去实现理想 敢于追随一切自己喜欢的 一定要身心独立，不做感情的傀儡",
        )
    }

    private val mItems = mutableListOf<ItemModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val circleAdapter = CircleAdapter(mItems)
        val loadMoreAdapter = LoadMoreAdapter.wrap(circleAdapter)
        binding.rvWxCircle.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = loadMoreAdapter
        }
        loadMoreAdapter.setOnLoadMoreListener {
            val items = createItems()
            mItems.addAll(items)
            loadMoreAdapter.finishLoadMore()
            circleAdapter.notifyItemRangeInserted(mItems.size - items.size, items.size)
        }

        val items = createItems()
        mItems.addAll(items)
        circleAdapter.notifyDataSetChanged()
    }

    //造数据
    private fun createItems(): List<ItemModel> {
        val items = mutableListOf<ItemModel>()
        for (i in 0 until 5) {
            val item = ItemModel(createImages(), createText())
            items.add(item)
        }
        return items
    }

    private fun createImages(): List<String> {
        val images = mutableListOf<String>()
        var randomCount = Random().nextInt(mImages.size)
//        randomCount = 1
//        val randomCount = 9
//        val randomCount = 4
        for (i in 0 until randomCount) {
            val index = Random().nextInt(mImages.size)
            images.add(mImages[index])
        }
        return images
    }

    private fun createText(): String {
        val randomIndex = Random().nextInt(mTexts.size)
        return mTexts[randomIndex]
    }

    data class ItemModel(
        val images: List<String>,
        val text: String
    )

    inner class CircleAdapter(
        private val items: List<ItemModel>
    ) : RecyclerView.Adapter<CircleViewHolder>() {

        override fun getItemCount() = items.size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CircleViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_circle, parent, false)
            return CircleViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: CircleViewHolder, position: Int) {
            val item = mItems[holder.adapterPosition]
            holder.tvDesc.text = item.text
            holder.tvNum.text = item.images.size.toString()

//            holder.nineGridView.adapter = Adapter(item.images)

            val imageAdapter = ImageAdapter(item.images, onBindView = { imageView, item, position ->
                Glide.with(imageView)
                    .load(item)
                    .centerCrop()
                    .placeholder(R.drawable.sp_loading)
                    .into(imageView)
            })
            imageAdapter.onItemViewClick = { url, position ->
                toast("ItemView click -- $position")
//                previewImage(item)
                previewImage(item.images, position)
            }
            imageAdapter.onExtraViewClick = { position ->
                toast("ExtraView click  $position")
            }

            holder.nineGridView.apply {
                adapter = imageAdapter
            }
        }
    }


    class CircleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDesc = itemView.findViewById<TextView>(R.id.tvDesc)
        val tvNum = itemView.findViewById<TextView>(R.id.tvNum)
        val nineGridView = itemView.findViewById<NineGridView>(R.id.nineGridView)
    }

    private fun previewImage(url: String) {
        StfalconImageViewer.Builder(this, mutableListOf(url)) { view, image ->
            Glide.with(view).load(image).into(view)
        }.show()
    }

    private fun previewImage(urls: List<String>, position: Int) {
        StfalconImageViewer.Builder(this, urls) { view, image ->
            Glide.with(view).load(image).into(view)
        }.show().setCurrentPosition(position)
    }

    inner class Adapter(
        private val items: List<String>
    ) : NineGridView.Adapter() {

        override fun getItemCount() = items.size

//        override fun adaptFourItem() = true

        override fun onCreateItemView(parent: ViewGroup, viewType: Int): View {
            return LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        }

        override fun onBindItemView(itemView: View, position: Int) {
            val imageView = itemView.findViewById<ImageView>(R.id.imageView)
            Glide.with(itemView)
                .load(items[position])
                .into(imageView)
            itemView.setOnClickListener {
                toast("itemView click")
            }
        }

        //
//        override fun adaptSingleView() = true

        override fun onCreateSingleView(parent: ViewGroup, viewType: Int): View? {
            return LayoutInflater.from(parent.context).inflate(R.layout.item_single, parent, false)
        }

        override fun onBindSingleView(singleView: View, position: Int) {
            val imageView = singleView.findViewById<ImageView>(R.id.imageView)
            Glide.with(singleView)
                .load(items[position])
                .into(imageView)
            singleView.setOnClickListener {
                toast("singleView click")
            }
        }

        //

//        override fun enableExtraView() = true

        override fun onCreateExtraView(parent: ViewGroup, viewType: Int): View? {
            return LayoutInflater.from(parent.context).inflate(R.layout.item_extra, parent, false)
        }

        override fun onBindExtraView(extraView: View, position: Int) {
            val tvExtra = extraView.findViewById<TextView>(R.id.tvExtra)
            val extraCount = items.size - position
            tvExtra.text = String.format("+%s", extraCount)
            extraView.setOnClickListener {
                toast("ExtraView click itemSize = ${items.size}")
            }
        }
    }

    fun toast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}