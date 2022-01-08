package com.example.store.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.store.databinding.ListWeighPriceCustomListBinding
import com.example.store.utils.Constant.TAG

class CustomListItemAdapter : RecyclerView.Adapter<CustomListItemAdapter.ViewHolder>() {

    private var addMoreWeightPrice: Int = 1
    private var weightList: MutableList<String> = mutableListOf()
    private var priceList: MutableList<String> = mutableListOf()

    class ViewHolder(val binding: ListWeighPriceCustomListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListWeighPriceCustomListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var weight: String = ""
        holder.binding.apply {
            iBtnClose.visibility = if (position > 0) View.VISIBLE else View.GONE
        }
    }

    override fun getItemCount() = addMoreWeightPrice

    fun setMapValue() {
        ++addMoreWeightPrice
        notifyDataSetChanged()
    }

    fun getWeightList() = weightList
    fun getPriceList() = priceList
}