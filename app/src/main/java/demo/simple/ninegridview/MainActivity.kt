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
import demo.simple.ninegridview.databinding.ActivityMainBinding
import me.simple.loadmoreadapter.LoadMoreAdapter
import me.simple.view.NineGridView
import java.util.*

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(this.layoutInflater) }

    private val mImages by lazy {
        mutableListOf(
            "https://gank.io/images/ce66aa74d78f49919085b2b2808ecc50",
            "https://gank.io/images/02eb8ca3297f4931ab64b7ebd7b5b89c",
            "https://gank.io/images/0f536c69ada247429b8a9e38d3dba8bb",
            "https://gank.io/images/ccf0316264d245018fc651cffa6e90de",
            "https://gank.io/images/95ddb01b6bd34a85aedfda4c9e9bd003",
            "https://gank.io/images/e92911f5ff9446d5a899b652b1934b93",
            "https://gank.io/images/6e57b254da79416bbe58248b570ea85f",
            "https://gank.io/images/e0b652d2a0cb46ba888a935c525bd312",
            "https://gank.io/images/95ddb01b6bd34a85aedfda4c9e9bd003",
            "https://gank.io/images/4817628a6762410895f814079a6690a1",
            "https://gank.io/images/0f536c69ada247429b8a9e38d3dba8bb",
            "https://gank.io/images/1a515f1508e345e2bf24673c2c2d50c4",
            "https://gank.io/images/4002b1fd18544802b80193fad27eaa62"
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
        val randomCount = Random().nextInt(mImages.size)
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
            holder.nineGridView.adapter = Adapter(item.images)
        }
    }


    class CircleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDesc = itemView.findViewById<TextView>(R.id.tvDesc)
        val nineGridView = itemView.findViewById<NineGridView>(R.id.nineGridView)
    }

    inner class Adapter(
        private val items: List<String>
    ) : NineGridView.Adapter() {

        override fun getItemCount(): Int {
            return items.size
        }

        override fun adaptFourItem() = true

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

        override fun adaptSingleView() = true

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

        override fun enableExtraView() = true

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