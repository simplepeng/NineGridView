package demo.simple.ninegridview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import demo.simple.ninegridview.databinding.ActivityMainBinding
import me.simple.view.NineGridView

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(this.layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.nineGridView.apply {
            adapter = Adapter()
        }
    }

    class Adapter : NineGridView.Adapter() {

        override fun getItemCount(): Int {
            return 10
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

        //

        override fun enableExtraView() = true

        override fun onCreateExtraView(parent: ViewGroup, viewType: Int): View? {
            return LayoutInflater.from(parent.context).inflate(R.layout.item_extra, parent, false)
        }
    }
}