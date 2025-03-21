package com.example.labs.ui.labFragments.lab3

import android.os.Bundle
import android.text.InputType
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.example.labs.R
import com.example.labs.data.GeneralFunctionsImpl
import com.example.labs.data.lab3.FeistelNetworkImpl
import com.example.labs.ui.GenerateKey
import com.example.labs.ui.GeneralViewModel
import com.example.labs.ui.MainLabsFragment

class Third: MainLabsFragment() {

    private val generalFunctions = GeneralFunctionsImpl
    private val feistelNetwork = FeistelNetworkImpl
    private val viewModel: GeneralViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            textForNameLab.setText(R.string.name_for_lab3)
            fieldForKey.setHint(R.string.count_of_rounds)
            btnForBottomDialog.isVisible = false

            btnEncryptOrDecrypt.setOnClickListener {
                startFeistelNetwork()
            }


            fieldForKey.setInputType(InputType.TYPE_CLASS_NUMBER)
        }
    }

    private fun startFeistelNetwork() {
            val message = binding.inputText.text.toString()
            val key = viewModel.generatedKey.toString()
            val rounds = binding.fieldForKey.text.toString().toIntOrNull() ?: 16
            val result = if (selectedOption()) {
                feistelNetwork.feistelNetworkEncrypt(message, key, rounds)
            } else {
                feistelNetwork.feistelFunctionDecrypt(message, key, rounds)
            }

            binding.outputText.text = result.trim()
            binding.outputText.isVisible = true
            binding.buttonCopy.isVisible = true

    }

    private fun showCreateBottomSheep() {
        val bottomSheet = GenerateKey()
        bottomSheet.show(parentFragmentManager, "FragmentGenerateKeyForLab3BottomDialogBinding")
    }

}