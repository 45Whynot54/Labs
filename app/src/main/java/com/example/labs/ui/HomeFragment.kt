package com.example.labs.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.labs.R
import com.example.labs.data.GeneralFunctionsImpl.showShortToast
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
        btnEmployed()

        binding.explanationImageView.setOnClickListener {
            showGitHub()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun btnEmployed() {
        val btnLabs = mapOf(
            binding.btnLab1 to R.id.action_homeFragment_to_lab1Fragment,
            binding.btnLab2 to R.id.action_homeFragment_to_secondLabFragment,
            binding.btnLab3 to R.id.action_homeFragment_to_third,
            binding.btnLab4 to R.id.action_homeFragment_to_fourth,
            binding.btnLab5 to R.id.action_homeFragment_to_fifth,
            binding.btnLab6 to R.id.action_homeFragment_to_sixth,
            binding.btnLab7 to R.id.action_homeFragment_to_seven,
            binding.btnLab8 to R.id.action_homeFragment_to_eighth

            )
        btnLabs.forEach { (button, actionId) ->
            button.setOnClickListener {
                findNavController().navigate(actionId)
            }
        }
    }


    private fun showGitHub() {
        val bottomSheetTwo = ExplanationBottomSheetFragment()
        bottomSheetTwo.show(childFragmentManager, "ExplanationBottomSheetFragment")
    }

}