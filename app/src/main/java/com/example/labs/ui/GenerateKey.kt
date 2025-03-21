package com.example.labs.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.labs.R
import com.example.labs.data.GeneralFunctionsImpl
import com.example.labs.data.lab3.FeistelNetworkKeyGenerator
import com.example.labs.databinding.FragmentGenerateKeyBottomDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class GenerateKey : BottomSheetDialogFragment() {

    private val generateKeyFeistelNetwork = FeistelNetworkKeyGenerator

    private val viewModel: KeyViewModel by activityViewModels()
    private val generalFunctions = GeneralFunctionsImpl



    private var _binding: FragmentGenerateKeyBottomDialogBinding? = null
    private val binding: FragmentGenerateKeyBottomDialogBinding
        get() = _binding
            ?: throw RuntimeException("FragmentGenerateKeyBottomDialogBinding == null")

    private var generateAction: (() -> String)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGenerateKeyBottomDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val labId = arguments?.getString(ARG_LAB_ID) ?: "Unknown"
        generateAction = when (labId) {
            "Lab1" -> {
                TODO()
            }

            "Lab2" -> {
                TODO()
            }

            "Lab3" -> {
                { generateKeyFeistelNetwork.key(null.toString(), null) }
            }

            "Lab4" -> {
                TODO()
            }

            "Lab5" -> {
                { generateKeyFeistelNetwork.key(null.toString(), null) }
            }

            "Lab6" -> {
                TODO()
            }

            "Lab7" -> {
                TODO()
            }

            "Lab8" -> {
                TODO()
            }
            else -> {
                {
                    generalFunctions.showShortToast(context, "Что-то пошло не так", 1000)
                    "Unknown lab"
                }
            }
        }

        with(binding) {
            createdKey.setHint(R.string.generated_key)
            extracted()

            keyTv.setText(R.string.key)
            btnGenerateKeys.setOnClickListener {
                gen()
            }
        }
    }

    private fun FragmentGenerateKeyBottomDialogBinding.extracted() {
        if (viewModel.generatedKey != null) {
            createdKey.text = viewModel.generatedKey.toString()
        }
    }

    private fun gen() {
        val genKey = generateAction?.invoke() ?: "No action"
        viewModel.generatedKey = genKey
        binding.createdKey.text = genKey
        generalFunctions.showShortToast(requireContext(), "${binding.createdKey.text.length}", 1000)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val ARG_LAB_ID = "labId"

        fun newInstance(labId: String): GenerateKey {
            val fragment = GenerateKey()
            val args = Bundle()
            args.putString(ARG_LAB_ID, labId)
            fragment.arguments = args
            return fragment
        }
    }
}