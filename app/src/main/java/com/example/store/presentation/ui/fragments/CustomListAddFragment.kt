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
import androidx.navigation.fragment.navArgs
import com.example.store.databinding.ListWeighPriceCustomListBinding
import com.example.store.presentation.ui.MainActivity
import com.example.store.presentation.ui.viewmodels.CustomListAddViewModel
import com.example.store.repository.data.entities.CustomListData
import com.example.store.utils.Constant.TAG
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CustomListAddFragment : Fragment() {
    private var _binding: FragmentCustomListAddBinding? = null
    private val binding get() = _binding!!
    private val args: CustomListAddFragmentArgs by navArgs<CustomListAddFragmentArgs>()
    private lateinit var addViewModel: CustomListAddViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCustomListAddBinding.inflate(layoutInflater, container, false)
        addViewModel = ViewModelProvider(this)[CustomListAddViewModel::class.java]
        (requireActivity() as MainActivity).setupActionBar(binding.includeToolbar.toolBar)
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
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.btnSave -> {
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
        }
        return super.onOptionsItemSelected(item)
    }

    private fun validateWeighAndPrice() {
        val priceList = mutableListOf<Int>()
        val weightList = mutableListOf<Int>()

        var v: View?
        val count = binding.priceWeightList.childCount

        for (i in 0 until count) {
            v = binding.priceWeightList.getChildAt(i)

            val weightEditText: TextInputLayout = v.findViewById(R.id.tilWeight)
            val priceEditText: TextInputLayout = v.findViewById(R.id.tilPrice)

            if (emptyTextFieldCheck(
                    priceEditText.editText!!.text.toString(),
                    weightEditText.editText!!.text.toString()
                )
            ) {
                Snackbar.make(
                    requireView(),
                    requireContext().resources.getString(R.string.error_input_empty),
                    Snackbar.LENGTH_LONG
                ).show()
                weightEditText.helperText =
                    requireContext().resources.getString(R.string.error_require)
                priceEditText.helperText =
                    requireContext().resources.getString(R.string.error_require)
                return
            }

            if (weightEditText.editText!!.text.isEmpty()) {
                Snackbar.make(
                    requireView(),
                    requireContext().resources.getString(R.string.error_input_empty),
                    Snackbar.LENGTH_LONG
                ).show()
                weightEditText.helperText =
                    requireContext().resources.getString(R.string.error_require)
                return
            }

            if (priceEditText.editText!!.text.isEmpty()) {
                Snackbar.make(
                    requireView(),
                    requireContext().resources.getString(R.string.error_input_empty),
                    Snackbar.LENGTH_LONG
                ).show()
                priceEditText.helperText =
                    requireContext().resources.getString(R.string.error_require)
                return
            }

            if (!emptyTextFieldCheck(
                    priceEditText.editText!!.text.toString(),
                    weightEditText.editText!!.text.toString()
                )
            ) {
                priceList.add(priceEditText.editText!!.text.toString().toInt())
                weightList.add(weightEditText.editText!!.text.toString().toInt())
            }
        }

        if (priceList.isEmpty() && weightList.isEmpty()) {
            Snackbar.make(
                requireView(),
                requireContext().resources.getString(R.string.error_enter_all_data),
                Snackbar.LENGTH_LONG
            ).show()
        } else {
            Log.i(TAG,"In else body ")
            val data = CustomListData(
                listName = binding.etItemName.text.toString(),
                priceList = priceList,
                weightList = weightList
            )
            Log.i(TAG,"In add list listName ${args.customListName} data = $data")
            addViewModel.insertCustomList(args.customListName,data)
            Snackbar.make(
                requireView(),
                "Save successfully",
                Snackbar.LENGTH_LONG
            ).show()
            removeAllView()
        }
    }

    private fun removeAllView() {
        binding.priceWeightList.removeAllViews()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}