package com.example.store.ui.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.store.R
import com.example.store.databinding.FragmentMainListBinding
import com.example.store.ui.dialogs.ListTypeDialog

class MainListFragment : Fragment() {
    private var _binding: FragmentMainListBinding? = null
    private val binding get() = _binding!!
    
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
        _binding = FragmentMainListBinding.inflate(layoutInflater,container,false)

        val listType = requireActivity().resources.getStringArray(R.array.list_type)
        var selectedTypePosition = 0

        binding.fButtonAdd.setOnClickListener {
            AlertDialog.Builder(requireActivity())
                .setTitle("List Type")
                .setSingleChoiceItems(listType,0){_,selectedPosition->
                    selectedTypePosition = selectedPosition
                }
                .setPositiveButton("Yes"){_,_->

                }
                .setNegativeButton("No"){dialog,_->
                    dialog.dismiss()
                }.show()
        }

        return binding.root
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}