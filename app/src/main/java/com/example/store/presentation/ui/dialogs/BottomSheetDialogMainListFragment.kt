package com.example.store.presentation.ui.dialogs

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.store.R
import com.example.store.repository.data.entities.MainList
import com.example.store.databinding.FragmentBottomSheetDialogMainListBinding
import com.example.store.presentation.ui.MainViewModel
import com.example.store.presentation.ui.utils.emptyTextFieldCheck
import com.example.store.utils.Constant.TAG
import com.example.store.utils.Resource
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import java.util.*


@AndroidEntryPoint
class BottomSheetDialogMainListFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetDialogMainListBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel
    private var rbCheckText: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            FragmentBottomSheetDialogMainListBinding.inflate(layoutInflater, container, false)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        binding.btnCancel.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnDone.setOnClickListener {
            binding.tilMainList.helperText = ""
            if (emptyTextFieldCheck(binding.etMainListName.text.toString())) {
                binding.tilMainList.helperText =
                    requireContext().resources.getString(R.string.error_input_empty)
            } else {
                viewModel.checkExistingMainList(binding.etMainListName.text.toString())
                checkExistingMainList()
            }
        }

        return binding.root
    }

    private fun checkExistingMainList() {
        lifecycleScope.launchWhenCreated {
            viewModel.checkExistingMainListStatus.collectLatest { resources ->
                val data = resources.data
                when (resources) {
                    is Resource.Success -> {
                        rbCheckText =
                            binding.rbGroup.findViewById<RadioButton>(binding.rbGroup.checkedRadioButtonId).text.toString()
                        data?.let {
                            if (binding.etMainListName.text.toString() == it.listName ) {
                                binding.tilMainList.helperText =
                                    requireContext().resources.getString(R.string.all_ready_exist)
                                binding.etMainListName.selectAll()
                                binding.etMainListName.isFocusable = true
                            } else {
                                Log.i(TAG, "in else body")
                                val mainList =
                                    MainList(
                                        listName = binding.etMainListName.text.toString()
                                            .replaceFirstChar {char ->
                                                if (char.isLowerCase()) char.titlecase(Locale.getDefault()) else char.toString()
                                            },
                                        listType = rbCheckText!!
                                    )
                                viewModel.addMainList(mainList = mainList)

                            }
                        }
                    }
                    is Resource.Loading -> {
                    }
                    is Resource.Error -> {
                        binding.tilMainList.helperText = resources.message
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}