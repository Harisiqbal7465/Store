package com.example.store.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.store.databinding.ListMainListBinding
import com.example.store.repository.data.entities.MainListData


class MainListInfoAdapter(
    private val listener :(MainListData) -> Unit
): RecyclerView.Adapter<MainListInfoAdapter.ViewHolder>(){

    class ViewHolder(val binding: ListMainListBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListMainListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = mainDataList[position]
        holder.binding.apply {
            tvMainListName.text = currentItem.listName
            tvMainListType.text = currentItem.listType
            mainList.setOnClickListener {
                listener(currentItem)
            }
        }
    }
    override fun getItemCount() = mainDataList.size


    private val diffCalBack = object: DiffUtil.ItemCallback<MainListData>(){
        override fun areItemsTheSame(oldItem: MainListData, newItem: MainListData): Boolean {
            return oldItem.listName == newItem.listName
        }

        override fun areContentsTheSame(oldItem: MainListData, newItem: MainListData): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCalBack)
    var mainDataList: List<MainListData>
        get() = differ.currentList
        set(value){ differ.submitList(value)}
}