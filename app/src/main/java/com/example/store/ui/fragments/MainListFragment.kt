package com.example.store.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.store.R
import com.example.store.databinding.FragmentMainListBinding
import com.example.store.ui.MainViewModel
import com.example.store.ui.adapters.MainListInfoAdapter
import com.example.store.utils.Resource
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

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
        _binding = FragmentMainListBinding.inflate(layoutInflater,container,false)

        val mainListAdapter = MainListInfoAdapter{listType ->
            if(listType == requireContext().resources.getString(R.string.custom_list)) {
                findNavController().navigate(R.id.action_mainListFragment_to_customListFragment)
            } else {
                findNavController().navigate(R.id.action_mainListFragment_to_companyListFragment)
            }
        }

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.getAllListOfMainList()
        lifecycleScope.launchWhenCreated {
            viewModel.listOfMainListStatus.collectLatest { resources ->
                when (resources) {
                    is Resource.Success -> {
                        binding.recyclerView.apply {
                            mainListAdapter.setValue(resources.data ?: emptyList())
                            adapter = mainListAdapter
                            layoutManager = LinearLayoutManager(requireContext())
                        }
                    }
                    is Resource.Loading -> {

                    }
                    is Resource.Error -> {
                        val bottomNavView: BottomNavigationView = activity?.findViewById(R.id.bottomNavigationView)!!
                        Snackbar.make(bottomNavView, resources.message ?: "An unexpected error occured", Snackbar.LENGTH_SHORT).apply {
                            anchorView = bottomNavView
                        }.show()
                    }
                }
            }
        }

        binding.fButtonAdd.setOnClickListener {
            findNavController().navigate(R.id.action_mainListFragment_to_bottomSheetDialogMainListFragment)
        }

        return binding.root
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
