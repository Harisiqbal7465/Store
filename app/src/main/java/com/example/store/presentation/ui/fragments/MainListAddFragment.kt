package com.example.store.presentation.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.store.R
import com.example.store.databinding.FragmentMainListAddBinding
import com.example.store.presentation.ui.utils.emptyTextFieldCheck
import com.example.store.presentation.ui.viewmodels.MainViewModel
import com.example.store.repository.data.entities.MainListData
import com.example.store.utils.Constant
import com.example.store.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import java.util.*


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainListAddFragment : Fragment() {

    private var _binding: FragmentMainListAddBinding? = null
    private val binding get() = _binding!!
    private var rbCheckText: String? = null
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainListAddBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.save_custom_list_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_close_24)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.btnSave -> {
                binding.tilMainList.helperText = ""
                if (emptyTextFieldCheck(binding.etMainListName.text.toString())) {
                    binding.tilMainList.helperText =
                        requireContext().resources.getString(R.string.error_input_empty)
                } else {

                    viewModel.checkExistingMainList(getFirstLetterCapital(binding.etMainListName.text.toString()))
                    checkExistingMainList()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getFirstLetterCapital(listName: String) = listName
        .replaceFirstChar { char ->
            if (char.isLowerCase()) char.titlecase(Locale.getDefault()) else char.toString()
        }

    private fun checkExistingMainList() {
        lifecycleScope.launchWhenCreated {
            viewModel.checkExistingMainListDataStatus.collectLatest { resources ->
                val data = resources.data
                when (resources) {
                    is Resource.Success -> {
                        rbCheckText =
                            binding.rbGroup.findViewById<RadioButton>(binding.rbGroup.checkedRadioButtonId).text.toString()
                        data?.let {
                            val listName =
                                getFirstLetterCapital(binding.etMainListName.text.toString())
                            if (resources.data) {
                                binding.tilMainList.helperText =
                                    requireContext().resources.getString(R.string.all_ready_exist)
                                binding.etMainListName.selectAll()
                                binding.etMainListName.isFocusable = true
                            } else {
                                val mainList =
                                    MainListData(
                                        listName = listName,
                                        listType = rbCheckText!!
                                    )
                                viewModel.addMainList(mainListData = mainList)

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