package com.example.store.presentation.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.store.R
import com.example.store.databinding.FragmentCustomListDetailBinding
import com.example.store.presentation.ui.adapters.WeighPriceListAdapter
import com.example.store.repository.data.entities.CustomListData
import com.example.store.utils.Constant.TAG

class CustomListDetailFragment : Fragment() {
    private var _binding: FragmentCustomListDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
        _binding = FragmentCustomListDetailBinding.inflate(layoutInflater,container,false)

        Log.i(TAG,"In custom list")
        val adapter = WeighPriceListAdapter(requireContext(), CustomListData("", listOf("Rs 20","20g"),
            listOf("19","2")))
        binding.listPriceWeight.adapter = adapter
       //(activity as AppCompatActivity).supportActionBar!!.hide()
        //(activity as AppCompatActivity).setSupportActionBar(binding.toolBar)
        binding.toolBar.apply {
            title = "List"
            setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as AppCompatActivity).supportActionBar!!.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity).supportActionBar!!.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}