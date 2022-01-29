package com.example.store.presentation.ui.adapters

import  android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.store.databinding.ListWeightPriceBinding
import com.example.store.repository.data.entities.CustomListData
import com.example.store.utils.Constant.TAG

class WeighPriceListAdapter(
    context: Context,
    val list: CustomListData
) : ArrayAdapter<CustomListData>(context, 0) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding: ListWeightPriceBinding
        var row = convertView
        Log.i(TAG,"in array adapter")
        if (row == null) {
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            binding = ListWeightPriceBinding.inflate(inflater, parent, false)
            row = binding.root
        } else {
            binding = ListWeightPriceBinding.bind(  row)
        }

        Log.i(TAG," price ${list.priceList[position]}")
        Log.i(TAG," weight ${list.weightList[position]}")

        binding.tvPrice.text = list.priceList[position]
        binding.tvWeight.text = list.weightList[position]
        return row
    }

    override fun getCount(): Int {
        return list.priceList.size
    }
}