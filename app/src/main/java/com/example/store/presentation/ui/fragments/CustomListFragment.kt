package com.example.store.presentation.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
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
import kotlinx.coroutines.flow.collect

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class CustomListFragment : Fragment() {
    private var _binding: FragmentCustomListBinding? = null
    private val binding get() = _binding!!
    private val args: CustomListFragmentArgs by navArgs()
    private lateinit var customListAdapter: ListCustomListAdapter
    private lateinit var viewModel: CustomListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCustomListBinding.inflate(layoutInflater, container, false)

        viewModel = ViewModelProvider(this)[CustomListViewModel::class.java]
        (requireActivity() as MainActivity).setupActionBar(binding.includeToolbar.toolBar)

        customListAdapter = ListCustomListAdapter {
            findNavController().navigate(
                CustomListFragmentDirections.actionCustomListFragmentToCustomListDetailFragment(it)
            )
        }

        adapterInilized()

        return binding.apply {
            includeToolbar.toolBar.apply {
                title = args.mainListData.listName
            }
            fbListAdd.setOnClickListener {
                findNavController().navigate(
                    CustomListFragmentDirections.actionCustomListFragmentToCustomListAddFragment(
                        args.mainListData.listName
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

    private fun adapterInilized() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.getCustomList.collect { resources ->
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
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}