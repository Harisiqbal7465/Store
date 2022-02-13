package com.example.store.presentation.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.store.R
import com.example.store.databinding.FragmentCustomListBinding
import com.example.store.presentation.ui.MainActivity
import com.example.store.presentation.ui.adapters.ListCustomListAdapter
import com.example.store.presentation.ui.viewmodels.CustomListViewModel
import com.example.store.utils.Resource
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class CustomListFragment : Fragment(), PopupMenu.OnMenuItemClickListener, SearchView.OnQueryTextListener{
    private var _binding: FragmentCustomListBinding? = null
    private val binding get() = _binding!!
    private val args: CustomListFragmentArgs by navArgs()
    private lateinit var viewModel: CustomListViewModel
    private lateinit var customListAdapter: ListCustomListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCustomListBinding.inflate(layoutInflater, container, false)

        (requireActivity() as MainActivity).setupActionBar(binding.includeToolbar.toolBar)
        viewModel = ViewModelProvider(this)[CustomListViewModel::class.java]

        adapterSetup()

        getListData()


        return binding.apply {
            includeToolbar.toolBar.apply {
                title = args.mainListData.listName
            }
            fbListAdd.setOnClickListener {
                findNavController().navigate(
                    CustomListFragmentDirections.actionCustomListFragmentToCustomListAddFragment(
                        args.mainListData.listName,
                        null
                    )
                )
            }
            includeToolbar.toolBar.setOnClickListener {
                findNavController().navigate(
                    CustomListFragmentDirections.actionCustomListFragmentToMainListDetailFragment(
                        args.mainListData
                    )
                )
            }
        }.root
    }

    private fun getListData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getCustomList.collectLatest { resources ->
                    when (resources) {
                        is Resource.Success -> {
                            binding.recyclerView.apply {
                                customListAdapter.customListData = resources.data ?: emptyList()
                                adapter = customListAdapter
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
        customListAdapter = ListCustomListAdapter(
            popupMenu = { view, currentList ->
                viewModel.setCurrentListId(currentList.listName)
                viewModel.setCurrentCustomListItem(currentList)
                val popupMenu = PopupMenu(requireContext(), view)
                popupMenu.setOnMenuItemClickListener(this)
                popupMenu.inflate(R.menu.custom_popup_menu)
                popupMenu.show()
            }
        ) {
            findNavController().navigate(
                CustomListFragmentDirections.actionCustomListFragmentToCustomListDetailFragment(it)
            )
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.info_item -> {
                findNavController().navigate(
                    CustomListFragmentDirections.actionCustomListFragmentToCustomListDetailFragment(
                        viewModel.currentCustomListItem!!
                    )
                )
            }
            R.id.delete_item -> {
                viewModel.deleteCustomList(args.mainListData.documentId, viewModel.currentListId)
            }
            R.id.edit_item -> {
                findNavController().navigate(
                    CustomListFragmentDirections.actionCustomListFragmentToCustomListAddFragment(
                        args.mainListData.documentId,
                        viewModel.currentCustomListItem
                    )
                )
            }
        }
        return false
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        query?.let {
            viewModel.searchCustomList(args.mainListData.documentId,it)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu,menu)
        val search = menu.findItem(R.id.btn_search)
        val searchView = search.actionView as? SearchView
        searchView?.setOnQueryTextListener(this)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}