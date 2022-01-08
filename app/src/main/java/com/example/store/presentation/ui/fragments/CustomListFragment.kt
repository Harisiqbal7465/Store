package com.example.store.presentation.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.store.R
import com.example.store.databinding.FragmentCustomListBinding

class CustomListFragment : Fragment() {
    private var _binding: FragmentCustomListBinding? = null
    private val binding get() = _binding!!
    
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
        _binding = FragmentCustomListBinding.inflate(layoutInflater,container,false)

        binding.fbListAdd.setOnClickListener {
            findNavController().navigate(R.id.action_customListFragment_to_customListAddFragment)
        }


        return binding.root
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}