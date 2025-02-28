package com.example.labs.ui.labFragments.lab3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.labs.R
import com.example.labs.data.GeneralFunctionsImpl
import com.example.labs.data.lab3.FeistelNetworkKeyGenerator
import com.example.labs.databinding.FragmentGenerateKeyForLab3BottomDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


open class GenerateKeyForLab3BottomDialog : BottomSheetDialogFragment() {

    private val generateKeyFeistelNetwork = FeistelNetworkKeyGenerator
    private val viewModel: FeistelKeyViewModel by activityViewModels()
    private val generalFunctions = GeneralFunctionsImpl

    private var _binding: FragmentGenerateKeyForLab3BottomDialogBinding? = null
    private val binding: FragmentGenerateKeyForLab3BottomDialogBinding
        get() = _binding
            ?: throw RuntimeException("FragmentGenerateKeyForLab3BottomDialogBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGenerateKeyForLab3BottomDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            createdKey.setHint(R.string.generated_key)

            extracted()

            keyTv.setText(R.string.key)
            btnGenerateKeys.setOnClickListener {
                gen()
            }
        }
    }

    private fun FragmentGenerateKeyForLab3BottomDialogBinding.extracted() {
        if (viewModel.generatedKey != null) {
            createdKey.text = viewModel.generatedKey.toString()
        }
    }

    private fun gen() {
        val genKey = generateKeyFeistelNetwork.keyGenerator()
        viewModel.generatedKey = genKey.toLong()
        binding.createdKey.text = genKey.toString()
        generalFunctions.showShortToast(requireContext(), "${binding.createdKey.text.length}", 1000)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}