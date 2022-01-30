package com.example.store.presentation.ui.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.store.R
import com.example.store.databinding.FragmentMainListBinding
import com.example.store.presentation.ui.MainActivity
import com.example.store.presentation.ui.viewmodels.MainViewModel
import com.example.store.presentation.ui.adapters.MainListInfoAdapter
import com.example.store.utils.Resource
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainListFragment : Fragment() {
    private var _binding: FragmentMainListBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainListBinding.inflate(layoutInflater, container, false)

        (requireActivity() as MainActivity).setupActionBar(binding.toolBar)

        val mainListAdapter = MainListInfoAdapter { listType, listName ->
            if (listType == requireContext().resources.getString(R.string.custom_list)) {
                findNavController().navigate(
                    MainListFragmentDirections.actionMainListFragmentToCustomListFragment(
                        listName
                    )
                )
            } else {
                findNavController().navigate(
                    MainListFragmentDirections.actionMainListFragmentToCompanyListFragment(
                        listName
                    )
                )
            }
        }

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        lifecycleScope.launchWhenCreated {
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
                            resources.message ?: "An unexpected error occured",
                            Snackbar.LENGTH_SHORT
                        ).apply {
                            anchorView = bottomNavView
                        }.show()
                    }
                }
            }
        }

        binding.fButtonAdd.setOnClickListener {
            findNavController().navigate(R.id.action_mainListFragment_to_mainListAddFragment)
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllListOfMainList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
