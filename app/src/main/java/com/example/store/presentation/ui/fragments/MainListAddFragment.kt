package com.example.store.presentation.ui.fragments

import  android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.store.R
import com.example.store.databinding.FragmentMainListAddBinding
import com.example.store.presentation.ui.MainActivity
import com.example.store.presentation.ui.utils.EditingState
import com.example.store.presentation.ui.utils.emptyTextFieldCheck
import com.example.store.presentation.ui.viewmodels.MainViewModel
import com.example.store.repository.data.entities.MainListData
import com.example.store.utils.Constant.TAG
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
    private lateinit var viewModel: MainViewModel
    private val args: MainListAddFragmentArgs by navArgs()
    private lateinit var editState: EditingState

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainListAddBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        (requireActivity() as MainActivity).setupActionBar(binding.includeToolbar.toolBar)

        editState =
            args.mainListId?.let { EditingState.EXISTING_MAIN_LIST } ?: EditingState.NEW_MAIN_LIST

        if (editState == EditingState.EXISTING_MAIN_LIST) {
            binding.apply {
                binding.includeToolbar.toolBar.title = "Edit List"
                etMainListName.setText(args.mainListId?.listName)
                val id = when (args.mainListId?.listName) {
                    requireContext().getString(R.string.custom_list) -> {
                        rbGroup.getChildAt(0).id
                    }
                    requireContext().getString(R.string.company_list) -> {
                        rbGroup.getChildAt(1).id
                    }
                    else -> {
                        rbGroup.getChildAt(0).id
                    }
                }
                rbGroup.check(id)
            }
        }

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
                saveButtonAction()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getFirstLetterCapital(listName: String) = listName
        .replaceFirstChar { char ->
            if (char.isLowerCase()) char.titlecase(Locale.getDefault()) else char.toString()
        }

    private fun saveButtonAction() {
        val listName = getFirstLetterCapital(binding.etMainListName.text.toString())
        val listType =
            binding.rbGroup.findViewById<RadioButton>(binding.rbGroup.checkedRadioButtonId).text.toString()
        val mainList =
            MainListData(
                listName = listName,
                listType = listType
            )
        when (editState) {
            EditingState.EXISTING_MAIN_LIST -> {
                viewModel.updateMainList(args.mainListId!!.documentId, mainList)
            }
            EditingState.NEW_MAIN_LIST -> {
                checkValidation {
                    viewModel.addMainList(mainListData = mainList)
                }
            }
        }
    }

    private fun checkValidation(saveAction: () -> Unit) {
        if (emptyTextFieldCheck(binding.etMainListName.text.toString())) {
            binding.tilMainList.helperText =
                requireContext().resources.getString(R.string.error_input_empty)
        } else {
            viewModel.checkExistingMainList(getFirstLetterCapital(binding.etMainListName.text.toString()))
            saveData(saveAction)
        }
    }

    private fun saveData(saveAction: () -> Unit) {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.checkExistingMainListDataStatus.collectLatest { resources ->
                val data = resources.data
                when (resources) {
                    is Resource.Success -> {

                        data?.let {
                            if (resources.data) {
                                Log.i(TAG, "already exist")
                                binding.tilMainList.helperText =
                                    requireContext().resources.getString(R.string.all_ready_exist)
                                binding.etMainListName.selectAll()
                                binding.etMainListName.isFocusable = true
                            } else {
                                Log.i(TAG, "in else block")
                                saveAction()
                                findNavController().popBackStack()
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