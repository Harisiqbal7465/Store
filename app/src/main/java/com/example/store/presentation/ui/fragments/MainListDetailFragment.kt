package com.example.store.presentation.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.Group
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.preference.PreferenceFragmentCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.store.R
import com.example.store.databinding.FragmentMainListDetailBinding
import com.example.store.presentation.ui.MainActivity
import com.example.store.presentation.ui.viewmodels.MainListDetailViewModel
import com.example.store.repository.data.entities.MainListData

import com.example.store.utils.Constant.TAG
import com.example.store.utils.Resource
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MainListDetailFragment : Fragment() {
    private var _binding: FragmentMainListDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainListDetailViewModel
    private val args: MainListDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainListDetailBinding.inflate(layoutInflater,container,false)
        viewModel = ViewModelProvider(this)[MainListDetailViewModel::class.java]

        (activity as MainActivity).setupActionBar(binding.includeToolbar.toolBar)

        /*viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.getMainListInfo.collectLatest { resources ->
                when (resources) {
                    is Resource.Success -> {
                        binding.tvListName.text = resources.data?.listName
                        binding.tvListType.text = resources.data?.listType
                       ("${resources.data?.listName} Info").also {
                           binding.toolBar.title = it
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
        }*/

        return binding.apply {
            ("${args.mainListId.listName} Info").also {
                binding.includeToolbar.toolBar.title = it
            }
            fbEditMainList.setOnClickListener {
                findNavController().navigate(
                    MainListDetailFragmentDirections.actionMainListDetailFragmentToMainListAddFragment(
                        args.mainListId
                    )
                )
            }
        }.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
/*override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
setPreferencesFromResource(R.xml.main_list_preferences, rootKey)
}*/
