package com.example.store.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.store.data.entities.MainList
import com.example.store.databinding.ListMainListBinding

class MainListInfoAdapter(
    private val listener :(String) -> Unit
): RecyclerView.Adapter<MainListInfoAdapter.ViewHolder>(){

    private var mainList = emptyList<MainList>()

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
                listener(currentItem.listType)
            }
        }
    }

    override fun getItemCount() = mainList.size
    fun setValue(list: List<MainList>) {
        this.mainList = list
        notifyDataSetChanged()
    }
}