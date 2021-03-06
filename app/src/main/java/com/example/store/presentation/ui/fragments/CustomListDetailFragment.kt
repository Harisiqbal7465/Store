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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.store.R
import com.example.store.databinding.FragmentCustomListDetailBinding
import com.example.store.presentation.ui.MainActivity
import com.example.store.presentation.ui.adapters.WeighPriceListAdapter
import com.example.store.utils.Constant.TAG

class CustomListDetailFragment : Fragment() {
    private var _binding: FragmentCustomListDetailBinding? = null
    private val binding get() = _binding!!
    private val args: CustomListDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCustomListDetailBinding.inflate(layoutInflater, container, false)
        (activity as MainActivity).setupActionBar(binding.includeToolbar.toolBar)
        val adapter = WeighPriceListAdapter(
            requireContext(), args.customList.weightList,
           args.customList.priceList
        )
        return binding.apply {
            listPriceWeight.adapter = adapter
            includeToolbar.toolBar.apply {
                title = args.customList.listName
                setNavigationOnClickListener {
                    findNavController().popBackStack()
                }
            }
            fbEditCustom.setOnClickListener {
                Log.i(TAG,"fab button click")
                val action = CustomListDetailFragmentDirections.actionCustomListDetailFragmentToCustomListAddFragment(
                    args.customList.listName,
                    args.customList
                )
                findNavController().navigate(action)
            }
        }.root
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