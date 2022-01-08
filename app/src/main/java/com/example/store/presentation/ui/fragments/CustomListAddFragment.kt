package com.example.store.presentation.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.store.databinding.FragmentCustomListAddBinding
import com.example.store.presentation.ui.adapters.CustomListItemAdapter
import com.example.store.utils.Constant.TAG

class CustomListAddFragment : Fragment() {
   private var _binding: FragmentCustomListAddBinding? = null
   private val binding get() = _binding!!

   override fun onCreateView(
           inflater: LayoutInflater,
           container: ViewGroup?,
           savedInstanceState: Bundle?
       ): View {
       _binding = FragmentCustomListAddBinding.inflate(layoutInflater,container,false)

       val customListAdapter = CustomListItemAdapter()

       binding.recyclerView.apply {
           adapter = customListAdapter
           layoutManager = LinearLayoutManager(requireContext())
       }

       binding.btnMore.setOnClickListener {
           customListAdapter.setMapValue()
       }

       binding.fbMic.setOnClickListener {
           val weightList = customListAdapter.getWeightList()
           val priceList = customListAdapter.getWeightList()
           Log.i(TAG,"weightList = $weightList and priceList = $priceList")
       }

       return binding.root
   }
   
   override fun onDestroyView() {
       super.onDestroyView()
       _binding = null
   }
}