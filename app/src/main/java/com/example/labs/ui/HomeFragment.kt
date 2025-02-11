package com.example.labs.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.labs.R
import com.example.labs.databinding.HomeFragmentBinding

class HomeFragment: Fragment() {

    private var _binding: HomeFragmentBinding ?= null
    private val binding: HomeFragmentBinding
        get() = _binding ?: throw RuntimeException("HomeFragmentBinding == null")


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLab1.setOnClickListener {
          findNavController().navigate(R.id.action_homeFragment_to_lab1Fragment)
        }
        binding.btnLab2.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_secondLabFragment)
        }
        binding.btnLab3.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_third)
        }
        binding.btnLab4.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_fourth)
        }
        binding.btnLab5.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_fifth)
        }
        binding.btnLab6.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_sixth)
        }
        binding.btnLab7.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_seventh)
        }
        binding.btnLab8.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_eighth)
        }

    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}