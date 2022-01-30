package com.example.store.presentation.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.store.databinding.ListCustomListBinding
import com.example.store.repository.data.entities.CustomListData


class ListCustomListAdapter(
    private val listener :(CustomListData) -> Unit
): RecyclerView.Adapter<ListCustomListAdapter.ViewHolder>(){

    class ViewHolder(val binding: ListCustomListBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListCustomListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = customListData[position]
        holder.binding.apply {
            tvCustomItemName.text = currentItem.listName
            Log.i("size","${currentItem.priceList[0]}")
            Log.i("size","${currentItem.weightList[0]}")
            ("Rs ${currentItem.priceList[0]}").also { tvPrice.text = it }
            ("${currentItem.weightList[0]}g").also { tvWeight.text = it }
            mainList.setOnClickListener {
                listener(currentItem)
            }
        }
    }
    override fun getItemCount() = customListData.size


    private val diffCalBack = object: DiffUtil.ItemCallback<CustomListData>(){
        override fun areItemsTheSame(oldItem: CustomListData, newItem: CustomListData): Boolean {
            return oldItem.listName == newItem.listName
        }

        override fun areContentsTheSame(oldItem: CustomListData, newItem: CustomListData): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCalBack)
    var customListData: List<CustomListData>
        get() = differ.currentList
        set(value){ differ.submitList(value)}
}