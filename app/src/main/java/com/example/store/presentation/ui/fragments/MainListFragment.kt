package com.example.store.presentation.ui.fragments

import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.store.R
import com.example.store.databinding.FragmentMainListBinding
import com.example.store.presentation.ui.MainActivity
import com.example.store.presentation.ui.adapters.MainListInfoAdapter
import com.example.store.presentation.ui.viewmodels.MainViewModel
import com.example.store.utils.Resource
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainListFragment : Fragment(), PopupMenu.OnMenuItemClickListener, SearchView.OnQueryTextListener {
    private var _binding: FragmentMainListBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel
    private lateinit var mainListAdapter: MainListInfoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainListBinding.inflate(layoutInflater, container, false)
        (requireActivity() as MainActivity).setupActionBar(binding.toolBar)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        adapterSetup()

        getListData()

        return binding.apply {
            fButtonAdd.setOnClickListener {
                findNavController().navigate(
                    MainListFragmentDirections.actionMainListFragmentToMainListAddFragment(null)
                )
            }
        }.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.info_item -> {
                findNavController().navigate(
                    MainListFragmentDirections.actionMainListFragmentToMainListDetailFragment(
                        viewModel.currentMainListItem!!
                    )
                )
            }
            R.id.delete_item -> {
                viewModel.deleteMainList(viewModel.currentListId)
            }
            R.id.edit_item -> {
                findNavController().navigate(
                    MainListFragmentDirections.actionMainListFragmentToMainListAddFragment(viewModel.currentMainListItem)
                )
            }
        }
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu,menu)
        val search = menu.findItem(R.id.btn_search)
        val searchView = search.actionView as? SearchView
        searchView?.setOnQueryTextListener(this)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        query?.let {
            viewModel.searchMainList(it)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return false
    }

    private fun getListData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.listOfMainListDataStatus.collectLatest { resources ->
                    when (resources) {
                        is Resource.Success -> {
                            binding.recyclerView.apply {
                                mainListAdapter.mainDataList = resources.data ?: emptyList()
                                adapter = mainListAdapter
                                layoutManager = LinearLayoutManager(requireContext())
                            }
                        }
                        is Resource.Loading -> {

                        }
                        is Resource.Error -> {
                            val bottomNavView: BottomNavigationView =
                                activity?.findViewById(R.id.bottomNavigationView)!!
                            Snackbar.make(
                                bottomNavView,
                                resources.message ?: "An unexpected error occurred",
                                Snackbar.LENGTH_SHORT
                            ).apply {
                                anchorView = bottomNavView
                            }.show()
                        }
                    }
                }
            }
        }
    }

    private fun adapterSetup() {
        mainListAdapter = MainListInfoAdapter(popupMenu = { view, currentList ->
            viewModel.setCurrentListId(currentList.documentId)
            viewModel.setCurrentMainListItem(currentList)
            val popupMenu = PopupMenu(requireContext(), view)
            popupMenu.setOnMenuItemClickListener(this)
            popupMenu.inflate(R.menu.main_popup_menu)
            popupMenu.show()

        }) { mainListData ->
            if (mainListData.listType == requireContext().resources.getString(R.string.custom_list)) {
                findNavController().navigate(
                    MainListFragmentDirections.actionMainListFragmentToCustomListFragment(
                        mainListData
                    )
                )
            } else {
                findNavController().navigate(
                    MainListFragmentDirections.actionMainListFragmentToCompanyListFragment(
                        mainListData.documentId
                    )
                )
            }
        }
    }


}


