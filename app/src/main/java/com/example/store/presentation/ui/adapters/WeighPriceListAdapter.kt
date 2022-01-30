package com.example.store.presentation.ui.adapters

import  android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.store.R
import com.example.store.databinding.ListWeightPriceBinding
import com.example.store.utils.Constant.TAG

class WeighPriceListAdapter(
    context: Context,
    private val weightList: List<Int>,
    private val priceList: List<Int>
) : ArrayAdapter<Int>(context, R.layout.list_weight_price,priceList) {

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
         ("Rs ${priceList[position]}").also {
             binding.tvPrice.text = it
         }
        ("${weightList[position]}g").also {
            binding.tvWeight.text = it
        }
        return row
    }

    override fun getCount(): Int {
        return priceList.size
    }
}
