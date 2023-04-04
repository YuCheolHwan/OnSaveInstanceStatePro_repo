package com.example.onsaveinstancestatepro

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.onsaveinstancestatepro.databinding.ItemRecyclerviewBinding

class MyAdapter(val dataList : MutableList<String>): RecyclerView.Adapter<MyAdapter.MyViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemRecyclerviewBinding = ItemRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(itemRecyclerviewBinding)
    }

    override fun getItemCount() = dataList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val itemRecyclerviewBinding = holder.itemRecyclerviewBinding
        itemRecyclerviewBinding.itemData.text = dataList.get(position).toString()
    }
    class MyViewHolder(val itemRecyclerviewBinding: ItemRecyclerviewBinding):RecyclerView.ViewHolder(itemRecyclerviewBinding.root){
    }
}