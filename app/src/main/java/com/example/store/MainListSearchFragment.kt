package com.example.store

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.store.databinding.FragmentMainListBinding
import com.example.store.databinding.FragmentMainListSearchBinding

class MainListSearchFragment : Fragment() {
    private var _binding: FragmentMainListSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainListSearchBinding.inflate(layoutInflater)

        return binding.apply {

        }.root
    }
}