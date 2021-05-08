package demo.simple.ninegridview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import demo.simple.ninegridview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(this.layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.nineGridView.apply {
            adapter = Adapter()
        }
    }

    class Adapter : RecyclerView.Adapter<VH>() {

        override fun getItemCount(): Int {
            return 5
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            val itemView =
                LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
            return VH(itemView)
        }

        override fun onBindViewHolder(holder: VH, position: Int) {
        }

    }

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView)
}