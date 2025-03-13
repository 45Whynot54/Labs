package com.example.labs.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.labs.data.GeneralFunctionsImpl
import com.example.labs.databinding.BottomSheetExplanationBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment



open class ExplanationBottomSheetFragment : BottomSheetDialogFragment() {


    private val generalFunctions = GeneralFunctionsImpl

    private var _binding: BottomSheetExplanationBinding? = null
    private val binding: BottomSheetExplanationBinding
        get() = _binding ?: throw RuntimeException("ExplanationForLabFragment == null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomSheetExplanationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.closeButton.setOnClickListener {
            dismiss()
        }
        binding.github.setOnLongClickListener {
            generalFunctions.copyText(requireContext(), binding.github.text.toString())
            true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}