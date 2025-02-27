package com.example.labs.ui.labFragments.lab3

import android.os.Bundle
import android.text.InputType
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.example.labs.R
import com.example.labs.data.GeneralFunctionsImpl
import com.example.labs.data.lab3.FeistelNetworkImpl
import com.example.labs.ui.MainLabsFragment

class Third: MainLabsFragment() {

    private val generalFunctions = GeneralFunctionsImpl
    private val feistelNetwork = FeistelNetworkImpl
    private val viewModel: FeistelKeyViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            textForNameLab.setText(R.string.name_for_lab3)
            fieldForKey.setHint(R.string.count_of_rounds)
            radioGroup.isVisible = false
            btnEncryptOrDecrypt.setOnClickListener {
                startFeistelNetwork()
            }
            btnForBottomDialog.setOnClickListener {
                showCreateBottomSheep()
            }
            fieldForKey.setInputType(InputType.TYPE_CLASS_NUMBER)
        }
    }
    private fun startFeistelNetwork(){
        val message = binding.inputText.text.toString()
        val key = viewModel.generatedKey?.toIntOrNull() ?: 16
        val rounds = binding.fieldForKey.text.toString().toIntOrNull() ?: 16
        val messageAsLong = message.hashCode()

        val result = feistelNetwork.feistelNetwork(messageAsLong, key, rounds)

        binding.outputText.text = result.toString(16)
        binding.outputText.isVisible = true
        binding.buttonCopy.isVisible = true

        generalFunctions.showShortToast(requireContext(), "${viewModel.generatedKey} и $key", 500)
    }

    private fun showCreateBottomSheep() {
        val bottomSheet = GenerateKeyForLab3BottomDialog()
        bottomSheet.show(parentFragmentManager, "FragmentGenerateKeyForLab3BottomDialogBinding")
    }
}