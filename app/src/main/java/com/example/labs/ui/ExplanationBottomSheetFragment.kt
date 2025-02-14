package com.example.labs.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.labs.R
import com.example.labs.databinding.BottomSheetExplanationBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment



open class ExplanationBottomSheetFragment : BottomSheetDialogFragment() {

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

        val labId = arguments?.getString(ARG_LAB_ID) ?: "Unknown"
        val code = when (labId) {
            "Lab1" -> R.string.code_lab1
            "Lab1Exp" -> R.string.explanation_lab1

            "Lab2" -> R.string.code_lab2
            "Lab2Exp" -> R.string.explanation_lab2

            "Lab3" -> R.string.code_lab3
            "Lab3Exp" -> R.string.explanation_lab3

            "Lab4" -> R.string.code_lab4
            "Lab4Exp" -> R.string.explanation_lab4

            "Lab5" -> R.string.code_lab5
            "Lab5Exp" -> R.string.explanation_lab5

            "Lab6" -> R.string.code_lab6
            "Lab6Exp" -> R.string.explanation_lab6

            "Lab7" -> R.string.code_lab7
            "Lab7Exp" -> R.string.explanation_lab7

            "Lab8" -> R.string.code_lab8
            "Lab8Exp" -> R.string.explanation_lab8

            else -> R.string.error_for_explatation
        }

        binding.explanationText.setText(code)
        binding.closeButton.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_LAB_ID = "labId"

        fun newInstance(labId: String): ExplanationBottomSheetFragment {
            val fragment = ExplanationBottomSheetFragment()
            val args = Bundle()
            args.putString(ARG_LAB_ID, labId)
            fragment.arguments = args
            return fragment
        }
    }
}