package com.example.chart2.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.chart2.R
import com.example.chart2.databinding.RvItemBinding
import com.example.chart2.models.RVItemData

interface ActionListener{
    fun setState(id: Int, state: Boolean)
}
class ItemRVAdapter(private val actionListener: ActionListener): ListAdapter<RVItemData, ItemRVAdapter.ViewHolder>(UserItemDiffCallBack()),
    View.OnClickListener {
    class ViewHolder(val binding: RvItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bindTo(data: RVItemData){
            with(binding){
                when (data.category){
                    "Кафе" -> { imageView.setImageResource(R.drawable.ic_baseline_local_cafe_24) }
                    "Продукты" -> { imageView.setImageResource(R.drawable.ic_baseline_fastfood_24) }
                    "Транспорт" -> { imageView.setImageResource(R.drawable.ic_baseline_directions_bus_24) }
                    "Онлайн-покупки" -> { imageView.setImageResource(R.drawable.ic_baseline_computer_24) }
                    "Канцелярия" -> { imageView.setImageResource(R.drawable.ic_baseline_create_24) }
                    "Кинотеатры" -> { imageView.setImageResource(R.drawable.ic_baseline_tv_24) }
                }
                textView.text = data.description
                textView2.text = data.price.toString()
                imageView2.tag = data
//                imageView2.setOnClickListener {
//                    if (data.state){
//                        actionListener.setState(data.id, false)
//                    } else{
//                        actionListener.setState(data.id, true)
//                    }

//                    Log.d("setState", (!data.state).toString())
//                }
                Log.d("RVAdapter", data.state.toString())
                if (data.state){
                    imageView2.setImageResource(R.drawable.ic_baseline_check_24)
                } else{
                    imageView2.setImageResource(R.drawable.ic_baseline_close_24)
                }
            }
        }
    }

    class UserItemDiffCallBack : DiffUtil.ItemCallback<RVItemData>(){
        override fun areItemsTheSame(
            oldItem: RVItemData,
            newItem: RVItemData
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: RVItemData,
            newItem: RVItemData
        ): Boolean = oldItem == newItem

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RvItemBinding.inflate(inflater, parent, false)
        binding.imageView2.setOnClickListener(this)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    override fun onClick(p0: View) {
        val data = p0.tag as RVItemData
        when (p0.id){
            R.id.imageView2 -> {
                actionListener.setState(data.id, !data.state)
            }
        }
    }
}