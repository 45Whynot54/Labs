package com.example.labs.ui.labFragments.lab3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.labs.R
import com.example.labs.data.lab3.FeistelNetworkKeyGenerator
import com.example.labs.databinding.FragmentGenerateKeyForLab3BottomDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class GenerateKeyForLab3BottomDialog : BottomSheetDialogFragment() {

    private val generateKeyFeistelNetwork = FeistelNetworkKeyGenerator
    private val viewModel: FeistelKeyViewModel by activityViewModels()
    private var keyGenerated: String? = null

    private var _binding: FragmentGenerateKeyForLab3BottomDialogBinding? = null
    private val binding: FragmentGenerateKeyForLab3BottomDialogBinding
        get() = _binding ?: throw RuntimeException("FragmentGenerateKeyForLab3BottomDialogBinding == null")

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

        viewModel.generatedKey.apply {
            keyGenerated
        }

        binding.keyTv.setText(R.string.key)
        binding.btnGenerateKeys.setOnClickListener {
            gen()
        }

    }

    private fun gen(){
        val genKey = generateKeyFeistelNetwork.keyGenerator()
        viewModel.generatedKey = genKey.toString()
        binding.createdKey.text = genKey.toString()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}