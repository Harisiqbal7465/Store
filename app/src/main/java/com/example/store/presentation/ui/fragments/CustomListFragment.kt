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
import kotlinx.coroutines.flow.collect

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
        (requireActivity() as MainActivity).setupActionBar(binding.toolBar)

        customListAdapter = ListCustomListAdapter {
            findNavController().navigate(
                CustomListFragmentDirections.actionCustomListFragmentToCustomListDetailFragment(it)
            )
        }

        adapterInilized()
        return binding.apply {
            fbListAdd.setOnClickListener {
                findNavController().navigate(
                    CustomListFragmentDirections.actionCustomListFragmentToCustomListAddFragment(
                        args.customListName
                    )
                )
            }
        }.root
    }

    private fun adapterInilized() {
        lifecycleScope.launchWhenCreated {
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
                            resources.message ?: "An unexpected error occured",
                            Snackbar.LENGTH_SHORT
                        ).apply {
                            anchorView = bottomNavView
                        }.show()
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllList(args.customListName)
    }

    override fun onStart() {
        super.onStart()
        adapterInilized()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}