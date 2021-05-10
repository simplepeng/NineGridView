package demo.simple.ninegridview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import demo.simple.ninegridview.databinding.ActivityMainBinding
import me.simple.view.ImageAdapter
import me.simple.view.NineGridView

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(this.layoutInflater) }

    private val images by lazy {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.nineGridView.apply {
//            adapter = Adapter()
            adapter = ImageAdapter(images)
        }
    }

    class Adapter : NineGridView.Adapter() {

        override fun getItemCount(): Int {
            return 1
        }

        override fun onCreateItemView(parent: ViewGroup, viewType: Int): View {
            return LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        }

        override fun onBindItemView(itemView: View, position: Int) {
        }

        //

        override fun adaptSingleView() = true

        override fun onCreateSingleView(parent: ViewGroup, viewType: Int): View? {
            return LayoutInflater.from(parent.context).inflate(R.layout.item_single, parent, false)
        }

        override fun onBindSingleView(singleView: View, position: Int) {
            super.onBindSingleView(singleView, position)

        }

        //

        override fun enableExtraView() = true

        override fun onCreateExtraView(parent: ViewGroup, viewType: Int): View? {
            return LayoutInflater.from(parent.context).inflate(R.layout.item_extra, parent, false)
        }

        override fun onBindExtraView(extraView: View, position: Int) {
            super.onBindExtraView(extraView, position)

        }
    }
}