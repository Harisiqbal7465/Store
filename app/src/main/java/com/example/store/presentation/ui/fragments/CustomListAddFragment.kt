package com.example.store.presentation.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import com.example.store.R
import com.example.store.databinding.FragmentCustomListAddBinding
import com.example.store.presentation.ui.utils.emptyTextFieldCheck
import com.google.android.material.snackbar.Snackbar
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.store.databinding.ListWeighPriceCustomListBinding
import com.example.store.presentation.ui.MainActivity
import com.example.store.presentation.ui.utils.EditingState
import com.example.store.presentation.ui.viewmodels.CustomListViewModel
import com.example.store.repository.data.entities.CustomListData
import com.example.store.utils.Constant.TAG
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class CustomListAddFragment : Fragment() {
    private var _binding: FragmentCustomListAddBinding? = null
    private val binding get() = _binding!!
    private val args: CustomListAddFragmentArgs by navArgs()
    private lateinit var viewModel: CustomListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCustomListAddBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(this)[CustomListViewModel::class.java]

        viewModel.setEditState(args.customListData?.let { EditingState.EXISTING_MAIN_LIST }
            ?: EditingState.NEW_MAIN_LIST)

        (requireActivity() as MainActivity).setupActionBar(binding.includeToolbar.toolBar)

        binding.includeToolbar.toolBar.title = when (viewModel.editState) {
            EditingState.EXISTING_MAIN_LIST -> "Edit Custom List"
            EditingState.NEW_MAIN_LIST -> "Create Custom List"
        }

        if (viewModel.editState == EditingState.EXISTING_MAIN_LIST) {
           editingInitialBind()
        }

        binding.btnMore.setOnClickListener {
            if (emptyTextFieldCheck(binding.etItemName.text.toString())) {
                Snackbar.make(
                    requireView(),
                    requireContext().resources.getString(R.string.error_input_empty),
                    Snackbar.LENGTH_LONG
                ).show()
                binding.tilItem.helperText =
                    requireContext().resources.getString(R.string.error_require)
            } else {
                binding.tilItem.isHelperTextEnabled = false
                addView()
            }
        }

        return binding.root
    }

    private fun editingInitialBind() {
        binding.etItemName.setText(args.customListData!!.listName)
        var v: View
        for (i in 0 until args.customListData!!.priceList.size) {
            addView()
            val itemPrice = args.customListData!!.priceList[i]
            val itemWeight = args.customListData!!.weightList[i]
            v = binding.priceWeightList.getChildAt(i)
            val weightEditText: TextInputLayout = v.findViewById(R.id.tilWeight)
            val priceEditText: TextInputLayout = v.findViewById(R.id.tilPrice)
            weightEditText.editText!!.setText(itemPrice.toString())
            priceEditText.editText!!.setText(itemWeight.toString())

        }
    }

    private fun addView() {
        val priceCustomListBinding = ListWeighPriceCustomListBinding.inflate(layoutInflater)
        priceCustomListBinding.iBtnClose.setOnClickListener {
            removeView(priceCustomListBinding.root)
        }
        binding.priceWeightList.addView(
            priceCustomListBinding.root,
            binding.priceWeightList.childCount
        )
    }

    private fun removeView(view: View) {
        binding.priceWeightList.removeView(view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_close_24)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.save_custom_list_menu, menu)
        if (viewModel.editState == EditingState.EXISTING_MAIN_LIST) {
            "Update".also {
                menu.findItem(R.id.btnSave).title = it
            }
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.btnSave -> {
                saveButtonAction()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveButtonAction() {
        if (emptyTextFieldCheck(binding.etItemName.text.toString())) {
            Snackbar.make(
                requireView(),
                requireContext().resources.getString(R.string.error_input_empty),
                Snackbar.LENGTH_LONG
            ).show()
            binding.tilItem.helperText =
                requireContext().resources.getString(R.string.error_require)
        } else {
            binding.tilItem.isHelperTextEnabled = false
            validateWeighAndPrice()
        }
    }

    private fun validateWeighAndPrice() {
        var v: View
        val count = binding.priceWeightList.childCount

        try {
            for (i in 0 until count) {
                v = binding.priceWeightList.getChildAt(i)
                val weightEditText: TextInputLayout = v.findViewById(R.id.tilWeight)
                val priceEditText: TextInputLayout = v.findViewById(R.id.tilPrice)

                if (priceEditText.editText!!.text.isEmpty() && weightEditText.editText!!.text.isEmpty()) {
                    weightEditText.helperText =
                        requireContext().resources.getString(R.string.error_require)
                    priceEditText.helperText =
                        requireContext().resources.getString(R.string.error_require)
                    return
                }

                if (weightEditText.editText!!.text.isEmpty()) {
                    weightEditText.helperText =
                        requireContext().resources.getString(R.string.error_require)
                    return
                }

                if (priceEditText.editText!!.text.isEmpty()) {
                    priceEditText.helperText =
                        requireContext().resources.getString(R.string.error_require)
                    return
                }

                if (!emptyTextFieldCheck(
                        priceEditText.editText!!.text.toString(),
                        weightEditText.editText!!.text.toString()
                    )
                ) {
                    viewModel.setPriceList(priceEditText.editText!!.text.toString().toInt())
                    viewModel.setWeightList(weightEditText.editText!!.text.toString().toInt())
                }
            }

        } catch (e: Exception) {
            Log.i(TAG,"error message = ${e.message}")
        }
        if (viewModel.weightList.isEmpty() && viewModel.priceList.isEmpty()) {
            Snackbar.make(
                requireView(),
                requireContext().resources.getString(R.string.error_enter_all_data),
                Snackbar.LENGTH_LONG
            ).show()
        } else {
            saveData()
        }

    }

    private fun saveData() {
        val data = CustomListData(
            listName = binding.etItemName.text.toString(),
            priceList = viewModel.priceList,
            weightList = viewModel.weightList
        )
        when (viewModel.editState) {
            EditingState.EXISTING_MAIN_LIST -> {
                viewModel.updateCustomList(args.customListName,args.customListData!!.listName,data)

            }
            EditingState.NEW_MAIN_LIST -> {
                viewModel.insertCustomList(args.customListName, data)
            }
        }
        removeAllView()
        findNavController().navigate(
            CustomListAddFragmentDirections.actionCustomListAddFragmentToCustomListDetailFragment(data)
        )
    }

    private fun removeAllView() {
        binding.priceWeightList.removeAllViews()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}