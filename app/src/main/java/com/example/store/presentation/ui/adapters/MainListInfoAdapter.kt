package com.example.store.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.store.repository.data.entities.MainList
import com.example.store.databinding.ListMainListBinding

class MainListInfoAdapter(
    private val listener :(String,String) -> Unit
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
        val currentItem = mainList[position]
        holder.binding.apply {
            tvCircleFirstText.text = currentItem.listName[0].toString()
            tvMainListName.text = currentItem.listName
            tvMainListType.text = currentItem.listType
            mainList.setOnClickListener {
                listener(currentItem.listType,currentItem.listName)
            }
        }
    }
    override fun getItemCount() = mainList.size


    private val diffCalBack = object: DiffUtil.ItemCallback<MainList>(){
        override fun areItemsTheSame(oldItem: MainList, newItem: MainList): Boolean {
            return oldItem.listName == newItem.listName
        }

        override fun areContentsTheSame(oldItem: MainList, newItem: MainList): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCalBack)
    var mainList: List<MainList>
        get() = differ.currentList
        set(value){ differ.submitList(value)}
}